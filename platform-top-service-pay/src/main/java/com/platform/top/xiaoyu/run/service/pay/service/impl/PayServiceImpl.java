package com.platform.top.xiaoyu.run.service.pay.service.impl;

import com.platform.top.xiaoyu.run.service.api.finance.enums.FsPayStatusEnums;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.feign.IPayFsFeignClient;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.callpay.CallPayReq;
import com.platform.top.xiaoyu.run.service.api.pay.enums.*;
import com.platform.top.xiaoyu.run.service.api.pay.exception.BasePayExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.utils.PaySignUtil;
import com.platform.top.xiaoyu.run.service.api.pay.vo.*;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayProductData;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayProductReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.PayBaseResp;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.PayResp;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.mango.MangoPayResp;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.okf.OkfPayResp;
import com.platform.top.xiaoyu.run.service.pay.constant.PayServiceConstant;
import com.platform.top.xiaoyu.run.service.pay.service.*;
import com.platform.top.xiaoyu.run.service.pay.service.mango.IPayApiMangoService;
import com.platform.top.xiaoyu.run.service.pay.service.okf.IPayApiOkfService;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.idx.IdService;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import com.top.xiaoyu.rearend.tool.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付系统对接 服务实现类
 *
 * @author coffey
 */
@Service
@Slf4j
public class PayServiceImpl implements IPayService {

	@Value("${pay.sysPath}")
	private String sysPath;

	@Autowired
	private IdService idService;
	@Autowired
	private IPayApiMangoService mangoService;
	@Autowired
	private IPayInfoService payInfoService;
	@Autowired
	private IPayFlowService iPayFlowService;
	@Autowired
	private IWhitelistService whitelistService;
	@Autowired
	private ITimeOutService timeOutService;
	@Autowired
	private IUserLvlService userLvlService;
	@Autowired
	private ITokenPayService tokenPayService;
	@Autowired
	private IPayFlowService flowService;
	@Autowired
	private IPayFsFeignClient fsFeignClient;
	@Autowired
	private IPayPlatformService platformService;
	@Autowired
	private IPayApiOkfService okfService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public PayResp paySingle(PayProductReq req) {
		PayReq payReq = BeanCopyUtils.copyBean(req, PayReq.class);
		PayProductData data = new PayProductData();
		data.setDesc(req.getProductDesc());
		data.setName(req.getProductName());
		data.setNum(req.getProductNum());
		data.setMoeny(req.getProductMoeny());

		List<PayProductData> list = new ArrayList<PayProductData>();
		list.add(data);
		payReq.setList(list);

		return this.pay(payReq);
	}

	/**
	 * 接收参数
	 * 调用第三方支付系统
	 * 返回数据： 即时返回、 异步通知、 异步页面
	 * 回写支付结果调用支付系统的上游系统
	 * @param req
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public PayResp pay(PayReq req) {

		this.checkPlatformId(req.getPlatformId());

		//数据清洗， 失败线程异常； 1.1 验证白名单 1.2 验证安全token 1.3 验证支付等级
		String sysKey = this.check(req.getUserId(), req.getPlatformId(), req.getSysToken());
		//验证支付平台， 验证签名， 返回 支付平台 与 支付方式
		PayPlatformVO findPlatformVO = this.checkPayPlatformSigin(req.getPayPlatformId(), sysKey, req);

		//请求方系统订单号
		String orderNo = req.getOrderNo();
		//当前支付系统 支付单号
		Long payNo = idService.getNextId();

		//支付平台key
		PayPlatformKeyEnums platformKey = PayPlatformKeyEnums.getType(findPlatformVO.getPayPlatfromKey());

		// 把支付单号设置成订单号， 用户调用支付接口
		req.setOrderNo(String.valueOf(payNo));

		//调用支付平台返回数据
		PayBaseResp payBaseResp = new PayBaseResp();

		//2. 根据不同接口类型，调用不同的接口
		if (platformKey == PayPlatformKeyEnums.MANGO) {
			payBaseResp = this.payMango(req, findPlatformVO.getPayType());
		} else if(platformKey == PayPlatformKeyEnums.OKF) {
			payBaseResp = this.payOkf(req, findPlatformVO.getPayType());
		}

		if (StringUtil.isEmpty(payBaseResp.getRespStr())) {
			throw new BizBusinessException(BaseExceptionType.RPC_CALL_DATA_NULL);
		}
		//插入支付单， 返回支付单据参数
		PayInfoVO payInfoVO = this.insertPayInfo(req, platformKey, sysKey, payNo, orderNo, req.getParam(), payBaseResp.getRespStr(), payBaseResp.getReqCallFlag(), payBaseResp.getRespFlagFail());

		//插入支付流水
		this.insertFlow(req, platformKey, payBaseResp.toString(), sysKey, payInfoVO);

		//返回到财务系统数据封装
		PayResp retResp = new PayResp();
		retResp.setStatussPay(payInfoVO.getStatussPay());

		retResp.setType(payBaseResp.getType().getValue());
		retResp.setUrl(payBaseResp.getUrl());
		retResp.setReqType(payBaseResp.getReqType().getValue());
		retResp.setReqParam(payBaseResp.getReqParam());
		retResp.setParam(req.getParam());
		retResp.setRemarks(req.getRemarks());
		retResp.setOrderNo(orderNo);
		retResp.setPayNo(payNo);
		retResp.setMoney(req.getMoney());
		retResp.setPayRemarks(payBaseResp.getRespRemarks());

		return retResp;
	}

	/**
	 * okf支付平台
	 * @param req
	 * @param payType
	 * @return
	 */
	private PayBaseResp payOkf(PayReq req, String payType) {
		PayBaseResp payBaseResp = new PayBaseResp();
		//除 100 万
		String money = new BigDecimal(req.getMoney()).divide(new BigDecimal(PayServiceConstant.UTILS_DIVISOR)).toString();
		OkfPayResp resp = okfService.payOkfPost(req, money, payType);
		log.info("返回OKF支付结果==》" + resp.toString());
		if(null != resp) {
			payBaseResp = BeanCopyUtils.copyBean(resp, PayBaseResp.class);
			//返回支付系统的所有参数
			payBaseResp.setRespStr(resp.toString());
			//返回支付系统的备注
			payBaseResp.setRespRemarks(resp.getMsg());
			//true 回调， false 不回调
			payBaseResp.setReqCallFlag(true);
			if(!resp.getState()) {
				// true 失败 false 成功
				payBaseResp.setRespFlagFail(true);
			} else {
				payBaseResp.setUrl(resp.getUrl());
			}
		}
		payBaseResp.setReqType(ReqTypeEnums.GET);
		payBaseResp.setType(RespTypeEnums.NOTIFY);
		return payBaseResp;
	}

	/**
	 * mango芒果支付平台
	 * @param req
	 * @param payType
	 * @return
	 */
	private PayBaseResp payMango(PayReq req, String payType) {
		PayBaseResp payBaseResp = new PayBaseResp();
		//除 100 万
		String money = new BigDecimal(req.getMoney()).divide(new BigDecimal(PayServiceConstant.UTILS_DIVISOR)).toString();

		// 请求芒果支付系统
		MangoPayResp resp = mangoService.payMangoPost(req, money, payType);
		log.info("返回MANGO支付结果==》" + resp.toString());
		if(null != resp) {
			payBaseResp = BeanCopyUtils.copyBean(resp, PayBaseResp.class);
			//返回支付系统的所有参数
			payBaseResp.setRespStr(resp.toString());
			//返回支付系统的备注
			payBaseResp.setRespRemarks(resp.getMsg());
			//true 回调， false 不回调
			payBaseResp.setReqCallFlag(true);
			if(!resp.getStatus().equals("1")) {
				// true 失败 false 成功
				payBaseResp.setRespFlagFail(true);
			} else {
				// 获得二维码路径
				payBaseResp.setUrl(resp.getUrl());
			}
		}
		payBaseResp.setReqType(ReqTypeEnums.POST);
		payBaseResp.setType(RespTypeEnums.NOTIFY);
		return payBaseResp;
	}

//	@Autowired
//	private FastFileStorageClient fastFileStorageClient;
//
//	private String imageUpload(MultipartFile file) {
//		InputStream in = null;
//		try {
//			String fileExtName = FilenameUtils.getExtension(file.getOriginalFilename());
//			long fileSize = file.getSize();
//			in = file.getInputStream();
//			StorePath path = this.fastFileStorageClient.uploadFile
//
//
//					(in, fileSize, fileExtName, (Set)null);
//		} catch (Exception var16) {
//			log.error(var16.getMessage(), var16);
//		} finally {
//			if (null != in) {
//				try {
//					in.close();
//				} catch (IOException var15) {
//					log.error(var15.getMessage(), var15);
//				}
//			}
//		}
//		return null;
//	}

	/**
	 * 插入支付单，支付流水, 返回支付状态
	 * @param req 支付请求
	 * @param reqSysKey 请求系统key
	 * @param platformKey 支付平台key
	 * @param payNo 支付单号
	 * @param orderNo 调用订单号
	 * @param respRemarks 返回信息
	 * @param reqCallFlag 回调通知标识 true 有回调通知
	 * @param respFlagFail 请求返回失败 true 失败
	 * @return 返回支付状态
	 */
	private PayInfoVO insertPayInfo(PayReq req, PayPlatformKeyEnums platformKey, String reqSysKey, Long payNo, String orderNo, String reqParam, String respRemarks, boolean reqCallFlag, boolean respFlagFail) {

		this.checkPlatformId(req.getPlatformId());

		/** 插入支付记录表 数据 */
		PayInfoVO infoVO = new PayInfoVO();
		infoVO.setReqRemarks(respRemarks);
		infoVO.setReqParam(reqParam);
		infoVO.setPlatformPayId(req.getPayPlatformId());

		infoVO.setPayAccount(req.getPayAccount());
		infoVO.setPayBank(req.getPayBank());
		infoVO.setPayBankCode(req.getPayBankCode());
		infoVO.setPayName(req.getPayName());

		infoVO.setReceiptAccount(req.getReceiptAccount());
		infoVO.setReceiptBank(req.getReceiptBank());
		infoVO.setReceiptBankCode(req.getReceiptBankCode());
		infoVO.setReceiptName(req.getReceiptName());

		infoVO.setPayNo(payNo);
		infoVO.setId(payNo);
		infoVO.setUserId(req.getUserId());
		infoVO.setPlatformId(req.getPlatformId());
		infoVO.setReqMoney(req.getMoney());
		infoVO.setReqOrderNo(orderNo);
		infoVO.setSysKey(reqSysKey);
		infoVO.setPlatformPayKey(platformKey.getValue());
		infoVO.setCreateTimestamp(LocalDateTime.now());

		// 返回成功
		infoVO.setStatussPay(StatusEnums.OK.getValue());

		if (respFlagFail) {
			// 返回失败
			infoVO.setStatussPay(StatusEnums.FAIL.getValue());
		} else {
			// 回调
			if (reqCallFlag) {
				infoVO.setStatussPay(StatusEnums.CALL.getValue());
			}
		}

		// 插入表
		payInfoService.insert(infoVO);

		return infoVO;
	}

	/**
	 * 插入支付流水
	 * @param req 请求参数
	 * @param platformKey 请求系统key，  OKF 支付平台， MANGO 支付平台
	 * @param respMsg 支付平台返回的所有参数
	 * @param reqSysKey 请求系统钥匙
	 * @param infoVO 支付单据
	 */
	private void insertFlow(PayReq req, PayPlatformKeyEnums platformKey, String respMsg, String reqSysKey, PayInfoVO infoVO ) {
		/** 4. 插入流水记录 */
		PayFlowVO flowVO = new PayFlowVO();

		flowVO.setParamReq(req.toString());
		flowVO.setParamResp(respMsg);
		flowVO.setCreateTimestamp(LocalDateTime.now());
		flowVO.setUserId(req.getUserId());
		flowVO.setPlatformId(req.getPlatformId());
		flowVO.setMoney(req.getMoney());
		flowVO.setType(FlowReqTypeEnums.TYPE_REQUEST.getValue());
		flowVO.setSysKey(reqSysKey);
		flowVO.setPlatformPayKey(platformKey.getValue());
		flowVO.setOrderNo(req.getOrderNo());
		flowVO.setPayNo(infoVO.getPayNo());
		flowVO.setStatuss(infoVO.getStatuss());
		flowVO.setStatussPay(infoVO.getStatussPay());
		flowVO.setPlatformPayId(infoVO.getPlatformPayId());

		flowService.insert(flowVO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean callPay(Long payNo, String amount, String reqStr, String attr) {
		//第三方接口回调成功，
		//1. 第三方请求回调， 查询接口表中的数据
		Long platformId = Long.parseLong(attr);
		PayInfoVO ret_vo = payInfoService.findDetailId(payNo, platformId);

		//验证数据已在库
		if (null == ret_vo) {
			throw new BizBusinessException(BasePayExceptionType.DATA_NULL);
		}

		//2 验证超时
		this.check_timeout(platformId, ret_vo.getCreateTimestamp(), ret_vo.getSysKey());

		//3. 更新 支付单状态
		PayInfoVO infoVO = new PayInfoVO();
		infoVO.setId(payNo);
		infoVO.setActualMoney(amount);
		infoVO.setStatuss(StatusEnums.OK.getValue());
		infoVO.setStatussPay(StatusEnums.OK.getValue());

		FsPayStatusEnums statusEnums = FsPayStatusEnums.getType(StatusEnums.OK.getValue());

		log.info("回调财务系统");
		//4 RPC 回调财务系统: 支付单号， 订单号，实际金额， 支付状态， 备注， 支付时间, 自定义参数返回值
		boolean call_flag = this.callFinance(ret_vo.getPayNo(), ret_vo.getReqOrderNo(), amount, statusEnums,
				ret_vo.getReqRemarks(), ret_vo.getCreateTimestamp(), attr,  ret_vo.getSysKey(), ret_vo.getUserId());

		if(!call_flag) {
			infoVO.setReqRemarks("回调财务系统出错");
		}

		//更新支付单状态
		payInfoService.update(infoVO);

		//5.写入流水日志
		PayFlowVO flowVO = new PayFlowVO();

		flowVO.setType(FlowReqTypeEnums.TYPE_RESP.getValue());
		flowVO.setParamReq(reqStr);
		flowVO.setCreateTimestamp(LocalDateTime.now());
		flowVO.setSysKey(ret_vo.getSysKey());
		flowVO.setUserId(ret_vo.getUserId());
		flowVO.setPlatformId(ret_vo.getPlatformId());
		flowVO.setMoney(amount);
		flowVO.setSysKey(ret_vo.getSysKey());
		flowVO.setOrderNo(ret_vo.getReqOrderNo());
		flowVO.setPayNo(ret_vo.getPayNo());
		flowVO.setStatuss(StatusEnums.OK.getValue());
		flowVO.setStatussPay(StatusEnums.OK.getValue());

		iPayFlowService.insert(flowVO);

		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String check(Long userId, Long platformId, String sysToken) {
		//2.1 验证白名单
		//2.2 验证安全token
		//2.3 验证支付等级

		//白名单
//		WhitelistVO whitelistVO = new WhitelistVO();
//		whitelistVO.setIp(SecurityUtil.getAccessToken().getCommonHeader().getIp());
//		whitelistService.findDetail(whitelistVO);

		//token 验证
		TokenPayVO tokenPayVO = new TokenPayVO();
		tokenPayVO.setToken(sysToken);
		tokenPayVO.setPlatformId(platformId);
		tokenPayVO = tokenPayService.findDetail(tokenPayVO);

		if (null == tokenPayVO) {
			throw new BizBusinessException(BasePayExceptionType.CHECK_TOKEN);
		}

		//查询用户等级
		UserLvlVO userLvlVO = new UserLvlVO();
		userLvlVO.setUserId(userId);
		userLvlVO.setSysKey(tokenPayVO.getSysKey());
		userLvlVO.setPlatformId(platformId);
		userLvlVO = userLvlService.findDetail(userLvlVO);

		if (null == userLvlVO) {
			//插入用户等级
			userLvlVO = new UserLvlVO();
			userLvlVO.setSysKey(tokenPayVO.getSysKey());
			userLvlVO.setLvl(1);
			userLvlVO.setNumber(10);
			userLvlVO.setUserId(userId);
			userLvlVO.setPlatformId(platformId);
			userLvlVO.setSysName(tokenPayVO.getSysName());

			userLvlService.insert(userLvlVO);
		}

		//查询接口等级
		TimeOutVO timeOutVO = new TimeOutVO();
		timeOutVO.setSysKey(tokenPayVO.getSysKey());
		timeOutVO.setPlatformId(platformId);
		timeOutVO = timeOutService.findDetail(timeOutVO);

		if ( null == timeOutVO ) {
			throw new BizBusinessException(BasePayExceptionType.CHECK_INTERFACELVL);
		}

		//用户接口等级与系统配置等级比较
		if ( timeOutVO.getLvl().intValue() != userLvlVO.getLvl().intValue() || timeOutVO.getLvl().intValue() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.CHECK_LVL);
		}
		return tokenPayVO.getSysKey();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void check_timeout(Long platformId, LocalDateTime reqDatetime, String sysKey) {
		//验证超时超时
		TimeOutVO timeOutVO = new TimeOutVO();
		timeOutVO.setSysKey(sysKey);
		timeOutVO.setPlatformId(platformId);
		timeOutVO = timeOutService.findDetail(timeOutVO);
		if (null == timeOutVO) {
			throw new BizBusinessException(BasePayExceptionType.CHECK_TIMEOUT);
		}

		//请求时间 大于设置时间，不能回调
		Duration duration = Duration.between(reqDatetime, LocalDateTime.now());
		long nanos = duration.toNanos();
		if( nanos > timeOutVO.getTimeOutSecond()) {
			throw new BizBusinessException(BasePayExceptionType.CHECK_TIMEOUT);
		}
	}

	/**
	 * 主动请求财务系统
	 * 内部系统请求
	 * @param payNo  支付单号
	 * @param orderNo 订单号
	 * @param amount 实际支付金额
	 * @param status 状态 true： 成功， false ： 失败
	 * @param remarks 支付备注
	 * @param create_date 支付时间
	 * @param attr 自定义参数返回值
	 * @return 返回成功、失败
	 */
	private boolean callFinance(Long payNo, String orderNo, String amount, FsPayStatusEnums status, String remarks,
								LocalDateTime create_date, String attr, String sysKey, Long userId) {

		//查询token
		TokenPayVO findTokenVO = new TokenPayVO();
		findTokenVO.setPlatformId(Long.parseLong(attr));
		findTokenVO.setSysKey(sysKey);
		findTokenVO = tokenPayService.findDetail(findTokenVO);

		if(null == findTokenVO) {
			throw new BizBusinessException(BaseExceptionType.PARAM_RPC_TOKEN_NULL);
		}

		//系统内部间调用, 回写状态给财务系统
		CallPayReq req = new CallPayReq();
		req.setAmount(amount);
		req.setOrderNo(orderNo);
		req.setPayDatetime(create_date);
		req.setPayNo(payNo);
		req.setStatusPay(status);
		req.setRemarks(remarks);
		//平台ID
		req.setAttr(attr);
		req.setUserId(userId);
		req.setToken(findTokenVO.getToken());

		Map<String, String> map = new HashMap<String, String>();
		map.put("platformid", attr);
		map.put("money", amount);
		map.put("orderno", orderNo);
		//密钥
		map.put("syskey", sysKey);

		//签名
		String signKey = PaySignUtil.sign(map);
		req.setSignKey(signKey);

		log.info("调用财务远程接口");
		R respR = fsFeignClient.callPay(req);
		if( respR.isSuccess() ) {
			return true;
		}
		return false;
	}

	/**
	 * 验证支付平台， 验证签名
	 * @param payPlatformId
	 */
	private PayPlatformVO checkPayPlatformSigin(Long payPlatformId, String sysKey, PayReq req) {

		PayPlatformVO payPlatformVO = platformService.findDetail(payPlatformId);
		if(null == payPlatformVO) {
			log.info("未查询到支付平台ID");
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		if(null == payPlatformVO.getEncrypt()) {
			log.info("加密方式不能为空");
			throw new BizBusinessException(BaseExceptionType.PARAM_ENCRYPT_FAIL);
		}
		//签名验证
		this.checkSign(req, sysKey, EncryptEnums.getType(payPlatformVO.getEncrypt()));

		return payPlatformVO;
	}

	/**
	 * 签名验证
	 * @param req 请求参数
	 * @param sysKey 商户密钥
	 * @param encryptEnums 加密方式
	 */
	private void checkSign(PayReq req, String sysKey, EncryptEnums encryptEnums) {

		if(null == encryptEnums) {
			throw new BizBusinessException(BaseExceptionType.PARAM_RPC_SIGN_NULL);
		}
		String amount = new BigDecimal(req.getMoney()).divide(new BigDecimal(PayServiceConstant.UTILS_DIVISOR)).toString();
		//签名
		Map<String, String> map = new HashMap<String, String>();
		map.put("platformid", req.getPlatformId().toString());
		map.put("money", amount);
		map.put("orderno", req.getOrderNo());
		map.put("systoken", req.getSysToken());
		map.put("syskey", sysKey);

		String signKey = PaySignUtil.sign(map);

		if(!signKey.equals(req.getSignKey())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_RPC_SIGN_ENCRYPT);
		}
	}

	private void checkPlatformId(Long platformId) {
		if(null == platformId || platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
	}

}
