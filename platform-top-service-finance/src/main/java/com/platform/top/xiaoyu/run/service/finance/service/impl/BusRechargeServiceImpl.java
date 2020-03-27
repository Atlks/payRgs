package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.finance.enums.*;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.stream.ActivityRechargeFirstMessage;
import com.platform.top.xiaoyu.run.service.api.finance.stream.EsRechargeFirstMessage;
import com.platform.top.xiaoyu.run.service.api.finance.stream.FinanceOutSource;
import com.platform.top.xiaoyu.run.service.api.finance.stream.TypeMessage;
import com.platform.top.xiaoyu.run.service.api.finance.vo.*;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.recharge.RechargeInsertOnlineReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.recharge.RechargeOnlineResp;
import com.platform.top.xiaoyu.run.service.api.pay.enums.RespTypeEnums;
import com.platform.top.xiaoyu.run.service.api.pay.feign.IPayFeignClient;
import com.platform.top.xiaoyu.run.service.api.pay.utils.PaySignUtil;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayProductReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.PayResp;
import com.platform.top.xiaoyu.run.service.finance.constant.BusConstant;
import com.platform.top.xiaoyu.run.service.finance.service.*;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.idx.IdService;
import com.top.xiaoyu.rearend.tool.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.chwin.firefighting.apiserver.QL.atiQlTokiz;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
/**
 * 充值 服务实现类
 *
 * @author coffey
 */
@Service
public class BusRechargeServiceImpl implements IBusRechargeService {

	@Value("${payToken.pay.token}")
	private String payToken;
	@Value("${payToken.pay.sysKey}")
	private String sysKey;

	@Autowired
	private IBusinessService busService;
	@Autowired
	private IFlowService flowService;
	@Autowired
	private IBookService iBookService;
	@Autowired
	private IReceiptAccountService accountService;
	@Autowired
	private IReceiptAccountToolsService toolsService;
	@Autowired
	private IdService idService;
	@Autowired
	private IPayFeignClient payFeignClient;

	@Override
	public Page<BusinessVO> findPage(Page<BusinessVO> page, BusinessVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		vo.setTypeAll(BusTypeAllEnums.RECHARGE.getVal());
		return busService.findPage(page, vo);
	}

	@Override
	public BusinessVO findDetail(BusinessVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		vo.setTypeAll(BusTypeAllEnums.RECHARGE.getVal());
		return busService.findDetail(vo);
	}

	@Override
	public BusinessVO findDetailId(Long id, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return busService.findDetailId(id, platformId);
	}

	@Override
	public List<BusinessVO> findListAll(BusinessVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		vo.setTypeAll(BusTypeAllEnums.RECHARGE.getVal());
		return busService.findListAll(vo);
	}

	/**
	 * 在线充值
	 * 1. 查询配置的收款账号， 及收款类型
	 * 2. 插入 充值记录表 数据
	 * 3. 判断收款类型，是否需要调用 rpc service 充值接口；
	 * 4. rpc 调用 支付系统 ： 充值接口 ==> 回调接口 / 即时接口
	 * 5.1 回调接口： 回调后修改 充值单据 状态： 入账成功/入账失败
	 * 5.2 即时接口： 修改 充值单据 状态： 入账成功/入账失败
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public RechargeOnlineResp insertOnline(RechargeInsertOnlineReq req, Long userId, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		/** 1. 查询配置的收款账号 */
		ReceiptAccountVO receiptAccountVO = accountService.findDetail(req.getAccountId(), platformId);
		if(null == receiptAccountVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_ACT_VALUE_NULL);
		}
		//查询 收款配置
		ReceiptAccountToolsVO toolsVO = toolsService.findDetail(receiptAccountVO.getToolsId(), platformId);
		if(null == toolsVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_ACT_TOOLS_NULL);
		}

		Long code = idService.getNextId();

		//插入充值 交易记录, 后台审核
		this.insertOnlineToReview(req, userId, platformId, code, receiptAccountVO);

		RechargeOnlineResp onlineResp = new RechargeOnlineResp();
		onlineResp.setType(1);

		//微信支付、 支付宝支付、 银联支付 == 》 跳转到 支付系统
		if ( BusTradingMannerEnums.getType(req.getTradingManner()) == BusTradingMannerEnums.BUS_TRA_WX_PAY
				|| BusTradingMannerEnums.getType(req.getTradingManner()) == BusTradingMannerEnums.BUS_TRA_ZFB_PAY
				|| BusTradingMannerEnums.getType(req.getTradingManner()) == BusTradingMannerEnums.BUS_TRA_BANK_RECHARGE
		) {
			if(StringUtil.isEmpty(toolsVO.getPayPlatformId())) {
				throw new BizBusinessException(BaseExceptionType.PARAM_PAYPLATFORMID_NULL);
			}
			//调用支付系统， 返回支付单数据 及 状态
			onlineResp = this.insertOnlineToPay(Long.parseLong(toolsVO.getPayPlatformId()), req, userId, platformId, code, receiptAccountVO);
		}

		//firest recharge event

		try{
//			 	Map m= Maps.newHashMap();
//			m.put("充值类型","在线充值");
//			 	m.put("user_id",userId);
//			m.put("platformId",platformId);
//			m.put("req",req);
//			m.put("onlineResp",onlineResp);
//			UserRegistMoney msg=new UserRegistMoney();
//			msg.setPlatformId(platformId);
//			msg.setUserId(userId);
//			msg.setRechargeType("在线充值");
//			firestecharge_event(msg);


			ActivityRechargeFirstMessage ActivityRechargeFirstMessage1=new ActivityRechargeFirstMessage();
			ActivityRechargeFirstMessage1.setUserId(userId);
			ActivityRechargeFirstMessage1.setPlatformId(platformId);
			ActivityRechargeFirstMessage1.setMoney(req.getAmount());


			EsRechargeFirstMessage EsRechargeFirstMessage1=new EsRechargeFirstMessage();
			EsRechargeFirstMessage1.setUserId(userId);
			EsRechargeFirstMessage1.setPlatformId(platformId);
			EsRechargeFirstMessage1.setMoney(req.getAmount());

			firestecharge_event(ActivityRechargeFirstMessage1,EsRechargeFirstMessage1);


		}catch (Exception e)
		{
			log.error("",e);
		}


		return onlineResp;
	}
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	Logger log = LoggerFactory.getLogger(atiQlTokiz.class);
	//首次充值事件
	private void firestecharge_event(ActivityRechargeFirstMessage msg, EsRechargeFirstMessage esRechargeFirstMessage1) {

		Map m3= Maps.newHashMap();
		m3.put("userId",msg.getUserId());
		m3.put("platformId",msg.getPlatformId());
		List<Map> li=    sqlSessionTemplate.selectList("recharge_count",m3);
		if(li.get(0).get("recharge_count").toString()=="1") {
			//	firestecharge_event(msg);

			//发送消息给活动
		//	msg.setTypeMessage(TypeMessage.TYPE__FINANCE_TO_ACT_FIRST_RECHARE);
			//  msg2.set
			FinanceOutSource1.financeToActivityOutput().send(MessageBuilder.withPayload(msg).setHeader(TypeMessage.MESSAGETYPE,TypeMessage.TYPE__FINANCE_TO_ACT_FIRST_RECHARE).build());


			//send to es
		//	msg.setTypeMessage(TypeMessage.TYPE_FINANCE_TO_ES_FIRST_RECHARE);
			FinanceOutSource1.financeToEsOutput().send(MessageBuilder.withPayload(esRechargeFirstMessage1).setHeader(TypeMessage.MESSAGETYPE,TypeMessage.TYPE_FINANCE_TO_ES_FIRST_RECHARE).build());


		}

	}

	//首次充值事件  dep
	private void firestecharge_event(Map msg) {

		//recharge_count_RecResult for first recharge active
	//	List<Map> recharge_count_RecResult = null;
	//		recharge_count_RecResult=session.selectList("recharge_count",msg);


		//recharge_count_RecResult for first recharge active

	//		if(recharge_count_RecResult.get(0).get("cnt").toString()=="1")
			{

				//发送消息给活动 TypeMessage.TYPE_ACTIVITY_FINANCE_FIST
				FinanceOutSource1.financeToActivityOutput().send(MessageBuilder.withPayload(msg).setHeader(TypeMessage.MESSAGETYPE, "8").build());

			}

	}

	/**
	 * 调用支付系统, 在线支付
	 * @param payPlatformId 支付平台ID
	 * @param req 充值请求
	 * @param userId 用户ID
	 * @param platformId 平台ID
	 * @param code 充值单
	 * @param receiptAccountVO 收款账号
	 */
	private RechargeOnlineResp insertOnlineToPay(Long payPlatformId, RechargeInsertOnlineReq req, Long userId, Long platformId, Long code, ReceiptAccountVO receiptAccountVO) {
		PayProductReq payReq = new PayProductReq();

		String amount = new BigDecimal(req.getAmount()).divide(new BigDecimal(BusConstant.UTILS_DIVISOR)).toString();

		//根据用户收款ID， 查询支付平台ID
		payReq.setPayPlatformId(payPlatformId);
		payReq.setSysToken(payToken);

		payReq.setParam(String.valueOf(platformId));
		payReq.setRemarks("在线充值");

		//付款人姓名或者后四位
		payReq.setPayName(req.getPayRemarks());
		payReq.setPayAccount(req.getPayRemarks());

		//收款人账号、卡号
		payReq.setReceiptAccount(receiptAccountVO.getAccountNo());
		//所属支行名称
		payReq.setReceiptBank(receiptAccountVO.getBankName());
		//收款账号所属支行CODE
		payReq.setReceiptBankCode(receiptAccountVO.getBankCode());
		//收款人名字
		payReq.setReceiptName(receiptAccountVO.getAccountName());

		payReq.setMoney(amount);
		payReq.setOrderNo(String.valueOf(code));

		payReq.setUserId(userId);
		payReq.setPlatformId(platformId);

		payReq.setProductName(BusConstant.RECHARGE_ONLINE);
		payReq.setProductNum(1);
		payReq.setProductMoeny(amount);
		payReq.setParam(String.valueOf(code));


		Map<String, String> map = new HashMap<String, String>();
		map.put("platformid", payReq.getPlatformId().toString());
		map.put("money", amount);
		map.put("orderno", payReq.getOrderNo());
		map.put("systoken", payReq.getSysToken());
		//密钥
		map.put("syskey", sysKey);


		//签名
		String signKey = PaySignUtil.sign(map);
		payReq.setSignKey(signKey);

		/** rpc 调用 支付系统 */
		R r_ret = payFeignClient.paySingle(payReq);

		if( !r_ret.isSuccess() ) {
			throw new BizBusinessException(r_ret.getCode(), "支付系统返回： ==> "+r_ret.getMsg());
		}

		PayResp pay_ret_resp = (PayResp) r_ret.getData();

		RechargeOnlineResp onlineResp = new RechargeOnlineResp();
		onlineResp.setType(pay_ret_resp.getType());
		//返回跳转支付页面
		if (RespTypeEnums.getType(pay_ret_resp.getType()) == RespTypeEnums.PAY
		|| RespTypeEnums.getType(pay_ret_resp.getType()) == RespTypeEnums.NOTIFY) {
			if (StringUtil.isEmpty(pay_ret_resp.getUrl())) {
				throw new BizBusinessException(BaseExceptionType.PAY_URL_NULL);
			}
			onlineResp.setUrl(pay_ret_resp.getUrl());
			onlineResp.setReqType(pay_ret_resp.getReqType());
			onlineResp.setReqParam(pay_ret_resp.getReqParam());
		}
		return onlineResp;
	}

	/**
	 * 插入充值 交易记录
	 * @param req 充值请求
	 * @param userId 用户ID
	 * @param platformId 平台ID
	 * @param code 充值单
	 * @param receiptAccountVO 收款账号
	 */
	private void insertOnlineToReview( RechargeInsertOnlineReq req, Long userId, Long platformId, Long code, ReceiptAccountVO receiptAccountVO ) {
		BusinessVO businessVO = new BusinessVO();
		businessVO.setCode(code);
		businessVO.setPayCode(code);
		businessVO.setUserId(userId);
		businessVO.setPlatformId(platformId);
		businessVO.setAmount(req.getAmount());
		// 支付状态： 等待
		businessVO.setPayStatus(FsPayStatusEnums.PAY_WAIT.getVal());
		businessVO.setPayTimestamp(LocalDateTime.now());
		//默认状态为 入账核查中
		businessVO.setTypeAll(BusTypeAllEnums.RECHARGE.getVal());
		//在线充值
		businessVO.setType(BusTypeEnums.RECHARGE_ONLINE.getVal());
		businessVO.setStatuss(BusStatusEnums.RECHARGE_DOING.getVal());
		businessVO.setTradingManner(req.getTradingManner());

		//付款人姓名/卡号后四位
		businessVO.setPayRemarks(req.getPayRemarks());
		//卡号
		businessVO.setPayAccount(req.getPayRemarks());
		//姓名
		businessVO.setPayName(req.getPayRemarks());

		//收款人账号、卡号
		businessVO.setReceiptAccount(receiptAccountVO.getAccountNo());
		//收款人支行code
		businessVO.setReceiptBank(receiptAccountVO.getBankCode());
		//收款人支行名称
		businessVO.setReceiptName(receiptAccountVO.getAccountName());

		//打码状态： 未同步
		businessVO.setStatusCode(BusStatusEnums.CODE_SYN_NULL.getValue());
		businessVO.setStatusCodeStr(BusStatusEnums.CODE_SYN_NULL.getName());

		/** 2. 插入充值数据 */
		if( !busService.insert(businessVO) ) {
			throw new BizBusinessException(BaseExceptionType.BUS_DATA_BUSINESS_INSERT_NULL);
		}

	}
	@Autowired
	SqlSession session;
	/**
	 * 页面审核 人工收款 功能：
	 * 1. 查询充值记录 是否存在
	 * 2. 插入流水记录 ： 人工入账
	 * 3. 修改 状态 ==》 入账成功
	 * 4. 可用余额累加
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateOk(Long id, Long platformId) {
		this.updateBusiness(platformId, id, FsPayStatusEnums.PAY_OK);

		return true;
	}

	/**
	 * 修改交易状态
	 * 插入交易流水
	 * @param platformId 平台ID
	 * @param id 交易ID 主键ID
	 * @param payStatusEnums 支付状态
	 */
	private void updateBusiness(Long platformId, Long id, FsPayStatusEnums payStatusEnums) {
		if (null != platformId && platformId.longValue() <= 0) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		/** 1. 查询充值记录 是否存在 */
		BusinessVO businessVO = this.findDetailId(id, platformId);
		if (null == businessVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		//当前状态必须为 101 入账核查
		if (businessVO.getStatuss().intValue() != BusStatusEnums.RECHARGE_DOING.getVal()) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAY_STATUS_NOT_101);
		}
		//查询我的账本
		BookVO bookVO = iBookService.findDetail(businessVO.getUserId(), businessVO.getPlatformId());
		if (null == bookVO) {
			throw new BizBusinessException(BaseExceptionType.BOOK_DATA_NULL);
		}


		//recharge_count_RecResult for first recharge active
//		List<Map> recharge_count_RecResult = null;
//try{
//
//	Map m= Maps.newHashMap();
//	m.put("user_id",businessVO.getUserId());
//	  recharge_count_RecResult=session.selectList("recharge_count",m);
//}catch (Exception e)
//{
//	log.error("",e);
//}



		BusinessVO updateBusVO = new BusinessVO();
		updateBusVO.setId(businessVO.getId());
		updateBusVO.setPlatformId(platformId);
		updateBusVO.setActualAmount(businessVO.getAmount());
		updateBusVO.setUpdateBy(SecurityUtil.getLoginAccount().getUserName());
		updateBusVO.setUpdateById(SecurityUtil.getLoginAccount().getUserId());
		updateBusVO.setPayTimestamp(LocalDateTime.now());
		updateBusVO.setUpdateTimestamp(LocalDateTime.now());
		// 默认， 不满足
		updateBusVO.setStatusCode(BusStatusEnums.CODE_SYN_NOT.getValue());
		updateBusVO.setStatusCodeStr(BusStatusEnums.CODE_SYN_NOT.getName());

		/**  2. 修改状态： 入账成功 */
		updateBusVO.setStatuss(BusStatusEnums.RECHARGE_OK.getVal());
		//支付状态： 成功
		updateBusVO.setPayStatus(FsPayStatusEnums.PAY_OK.getVal());
		if (!busService.update(updateBusVO)) {
			throw new BizBusinessException(BaseExceptionType.BOOK_DATA_UPDATE_STATUS);
		}

		/**  3. 可用余额累加  */
		if (!iBookService.addBalanceRecharge(bookVO.getId(), platformId, businessVO.getAmount())) {
			throw new BizBusinessException(BaseExceptionType.DATA_BALANCE);
		}

		if (payStatusEnums == FsPayStatusEnums.PAY_OK) {
			/** 4. 插入交易流水 */
			FlowVO flowVO = new FlowVO();
			flowVO.setStatuss(updateBusVO.getStatuss());
			flowVO.setAmount(businessVO.getAmount());
			flowVO.setActualAmount(businessVO.getAmount());
			flowVO.setUserId(businessVO.getUserId());
			flowVO.setPlatformId(businessVO.getPlatformId());
			flowVO.setBusTimestamp(LocalDateTime.now());
			//会员充值
			flowVO.setTypeAll(BusTypeAllEnums.RECHARGE.getVal());
			//充值记录表中充值类型
			flowVO.setType(businessVO.getType());
			flowVO.setCodeId(businessVO.getId());
			flowVO.setCode(String.valueOf(businessVO.getPayCode()));
			flowVO.setCodeOther(String.valueOf(businessVO.getPayCode()));
			//系统入账
			flowVO.setTradingManner(BusTradingMannerEnums.BUS_TRA_ADMIN_IN.getVal());

			//人工充值
			flowVO.setRemark(BusConstant.RECHARGE_MANUAL_REVIEW);
			flowVO.setDescription(BusConstant.RECHARGE_MANUAL_REVIEW);

			//在线充值
			if (BusTypeEnums.getType(businessVO.getType()) == BusTypeEnums.RECHARGE_ONLINE) {
				flowVO.setRemark(BusConstant.RECHARGE_ONLINE_REVIEW);
				flowVO.setDescription(BusConstant.RECHARGE_ONLINE_REVIEW);
			}

			//付款信息
			flowVO.setPayName(businessVO.getPayName());
			flowVO.setPayAccount(businessVO.getPayAccount());
			flowVO.setPayBankCode(businessVO.getPayBankCode());
			flowVO.setPayBank(businessVO.getPayBank());
			flowVO.setPayRemarks(businessVO.getPayRemarks());

			//收款人姓名
			flowVO.setReceiptName(businessVO.getReceiptName());
			//收款人账号、卡号
			flowVO.setReceiptAccount(businessVO.getReceiptAccount());
			//收款人支行code
			flowVO.setReceiptBankCode(businessVO.getReceiptBankCode());
			//收款人支行名称
			flowVO.setReceiptBank(businessVO.getReceiptBank());

			//插入交易流水
			if (!flowService.insertFlow(flowVO)) {
				throw new BizBusinessException(BaseExceptionType.FLOW_FAIL);
			}
		}


//recharge_count_RecResult for first recharge active
//		try{
//			if(recharge_count_RecResult.get(0).get("cnt").toString()=="0")
//			{
//				Map msg=Maps.newHashMap();
//				msg.put("platformId",platformId);
//				msg.put("充值记录",businessVO);  //里面包括了人员信息userid等
//				//发送消息给ES
//				FinanceOutSource1.financeToActivityOutput().send(MessageBuilder.withPayload(msg).setHeader(TypeMessage.MESSAGETYPE, TypeMessage.TYPE_ACTIVITY_FINANCE_FIST).build());
//
//			}
//		}catch(Exception e)
//		{
//			log.error("",e);
//		}

		//firest recharge event

		try{
//			Map msg= Maps.newHashMap();
//			msg.put("充值类型","人工充值");
//			msg.put("充值记录",businessVO);  //里面包括了人员信息userid等
//
//			msg.put("platformId",platformId);


//			UserRegistMoney msg=new UserRegistMoney();
//			msg.setPlatformId(platformId);
//			msg.setUserId(businessVO.getUserId());
//			msg.setRechargeType("人工充值");

			ActivityRechargeFirstMessage ActivityRechargeFirstMessage1=new ActivityRechargeFirstMessage();
			ActivityRechargeFirstMessage1.setUserId(businessVO.getUserId());
			ActivityRechargeFirstMessage1.setPlatformId(platformId);

			ActivityRechargeFirstMessage1.setMoney(businessVO.getAmount());


			EsRechargeFirstMessage EsRechargeFirstMessage1=new EsRechargeFirstMessage();
			EsRechargeFirstMessage1.setUserId(businessVO.getUserId());
			EsRechargeFirstMessage1.setPlatformId(platformId);
			EsRechargeFirstMessage1.setMoney( businessVO.getAmount());

			firestecharge_event(ActivityRechargeFirstMessage1,EsRechargeFirstMessage1);



		}catch (Exception e)
		{
			log.error("",e);
		}



	}

@Autowired
	FinanceOutSource FinanceOutSource1;
	/**
	 * 支付系统回调，返回
	 * 	 * 修改交易记录 支付状态
	 * 	 * 支付成功： 插入交易记录
	 * @param id 交易记录表 主键ID
	 * @param platformId 平台ID
	 * @param amount 实际交易金额
	 * @param payStatusEnums 支付状态： 成功、失败
	 * @return
	 */
	@Override
	public boolean updateSysCall(Long id, Long platformId, String amount, FsPayStatusEnums payStatusEnums) {
		this.updateBusiness(platformId, id, payStatusEnums);
		return true;
	}

	@Override
	public boolean insertNotOnline(Long userId, Long platformId, String amount, String name, Long accountId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		/** 1. 查询配置的收款账号 */
		ReceiptAccountVO toolsBankVO = accountService.findDetail(accountId, platformId);
		if(null == toolsBankVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_ACT_VALUE_NULL);
		}

		BookVO bookVO = iBookService.findDetail(userId, platformId);
		if(null == bookVO) {
			throw new BizBusinessException(BaseExceptionType.BOOK_DATA_NULL);
		}

		Long ran_code = idService.getNextId();

		BusinessVO businessVO = new BusinessVO();
		businessVO.setCode(ran_code);
		businessVO.setPayCode(ran_code);
		businessVO.setUserId(userId);
		businessVO.setPlatformId(platformId);
		businessVO.setAmount(amount);
		//入账核查中
		businessVO.setStatuss(BusStatusEnums.RECHARGE_DOING.getVal());
		businessVO.setPayTimestamp(LocalDateTime.now());
		//系统入账
		businessVO.setTradingManner(BusTradingMannerEnums.BUS_TRA_ADMIN_IN.getVal());
		//会员充值
		businessVO.setTypeAll(BusTypeAllEnums.RECHARGE.getVal());
		//人工存入
		businessVO.setType(BusTypeEnums.RECHARGE_OFFLINE.getVal());
		//支付状态： 等待
		businessVO.setPayStatus(FsPayStatusEnums.PAY_WAIT.getVal());

		//付款人姓名
		businessVO.setPayName(name);
		businessVO.setPayRemarks(name);

		//收款人账号、卡号
		businessVO.setReceiptAccount(toolsBankVO.getAccountNo());
		//收款人支行code
		businessVO.setReceiptBank(toolsBankVO.getBankCode());
		//收款人支行名称
		businessVO.setReceiptName(toolsBankVO.getAccountName());

		//未同步
		businessVO.setStatusCode(BusStatusEnums.CODE_SYN_NULL.getValue());
		businessVO.setStatusCodeStr(BusStatusEnums.CODE_SYN_NULL.getName());

		return busService.insert(businessVO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateFail(Long id, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		/** 1. 查询充值记录 是否存在 */
		BusinessVO find_vo = this.findDetailId(id, platformId);

		this.checkBusinessVO(find_vo);

		/** 2. 更新状态 入账取消 */
		BusinessVO update_vo = new BusinessVO();
		update_vo.setId(find_vo.getId());
		update_vo.setPlatformId(platformId);
		//入账取消
		update_vo.setStatuss(BusStatusEnums.RECHARGE_CANCEL.getVal());
		update_vo.setPayStatus(FsPayStatusEnums.PAY_FAIL.getVal());
		update_vo.setUpdateById(SecurityUtil.getLoginAccount().getUserId());
		update_vo.setUpdateBy(SecurityUtil.getLoginAccount().getUserName());
		update_vo.setUpdateTimestamp(LocalDateTime.now());

		return busService.update(update_vo);

	}

	/**
	 * 判断查询数据不为空
	 * @param businessVO
	 */
	private void checkBusinessVO(BusinessVO businessVO) {
		//判断取款数据不为空
		if(null == businessVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		//判断状态 == 入账核查中
		if(businessVO.getStatuss().intValue() != BusStatusEnums.RECHARGE_DOING.getVal()) {
			throw new BizBusinessException(BaseExceptionType.DATA_VERIFICATION);
		}
	}

}
