package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.common.enums.CommonStatus;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.finance.enums.*;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.*;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.extract.ExtractNotOnlineInsertReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.extract.ExtractReviewManualInsertReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.recharge.RechargeOnlineResp;
import com.platform.top.xiaoyu.run.service.api.pay.enums.RespTypeEnums;
import com.platform.top.xiaoyu.run.service.api.pay.feign.IPayFeignClient;
import com.platform.top.xiaoyu.run.service.api.pay.utils.PaySignUtil;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayProductData;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.PayResp;
import com.platform.top.xiaoyu.run.service.finance.constant.BusConstant;
import com.platform.top.xiaoyu.run.service.finance.service.*;
import com.top.xiaoyu.rearend.component.lock.api.annotation.Lock;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.idx.IdService;
import com.top.xiaoyu.rearend.tool.util.lang.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提现、取款 服务实现类
 *
 * @author coffey
 */
@Service
public class BusExtractServiceImpl implements IBusExtractService {

	@Value("${payToken.pay.sysKey}")
	private String sysKey;
	@Value("${payToken.pay.token}")
	private String payToken;

	@Autowired
	private IBusinessService busService;
	@Autowired
	private IdService idService;
	@Autowired
	private IFlowService flowService;
	@Autowired
	private IBookService iBookService;
	@Autowired
	private IBankBindingService bindingService;
	@Autowired
	private IPayFeignClient payFeignClient;
	@Autowired
	private IPayFsService payFsService;
	@Autowired
	private IFreezeOrderService iFreezeOrderService;

	@Override
	public Page<BusinessVO> findPage(Page<BusinessVO> page, BusinessVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		vo.setTypeAll(BusTypeAllEnums.EXTRACT.getVal());
		return busService.findPage(page, vo);
	}

	@Override
	public List<BusinessVO> findListAll(BusinessVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		vo.setTypeAll(BusTypeAllEnums.EXTRACT.getVal());
		return busService.findListAll(vo);
	}

	@Override
	public BusinessVO findDetail(BusinessVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		vo.setTypeAll(BusTypeAllEnums.EXTRACT.getVal());
		return busService.findDetail(vo);
	}

	@Override
	public BusinessVO findDetailId(Long id, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		BusinessVO vo = new BusinessVO();
		vo.setId(id);
		vo.setPlatformId(platformId);
		vo.setTypeAll(BusTypeAllEnums.EXTRACT.getVal());
		return busService.findDetail(vo);
	}

	/**
	 * 人工取款， 线下交易一笔
	 * 可用余额 累减， 冻结金额累加
	 * 插入一条记录到交易表
	 * 插入交易流水记录
	 * 插入冻结表
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@Lock("coffey-finance-userId-#{#req.userId}")
	public boolean insertNotOnline(ExtractNotOnlineInsertReq req, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		/** 查询我的账本 */
		BookVO bookVO = iBookService.findDetail(req.getUserId(), platformId);
		if(null == bookVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		/** 可用余额 累减， 冻结金额累加 */
		if(!iBookService.addBalanceExtractReg(bookVO.getId(), bookVO.getPlatformId(), req.getAmount())) {
			//可用余额不足， 扣减不成功
			throw new BizBusinessException(BaseExceptionType.DATA_BALANCE_NOTONLINE);
		}
		/** 查询用户绑定的银行卡 */
		BankBindingVO bindingVO = new BankBindingVO();
		bindingVO.setUserId(req.getUserId());
		bindingVO.setPlatformId(platformId);
		bindingVO.setStatuss(CommonStatus.ENABLE.getValue());
		// 1 启用， 2 禁用
		bindingVO.setIsDeleted(CommonStatus.ENABLE.getValue());
		bindingVO = bindingService.findDetail(bindingVO);
		if( null == bindingVO) {
			throw new BizBusinessException(BaseExceptionType.BINDING_DATA_NULL);
		}

		BusinessVO businessVO = new BusinessVO();
		Long ran_code = idService.getNextId();
		businessVO.setId(ran_code);
		businessVO.setCode(ran_code);
		businessVO.setPayCode(ran_code);

		businessVO.setUserId(req.getUserId());
		businessVO.setPlatformId(platformId);
		businessVO.setAmount(req.getAmount());
		//正在出款
		businessVO.setStatuss(BusStatusEnums.EXTRACT_DOING.getVal());
		//支付状态： 等待支付
		businessVO.setPayStatus(FsPayStatusEnums.PAY_WAIT.getVal());
		businessVO.setPayTimestamp(LocalDateTime.now());
		//交易方式
		businessVO.setTradingManner(req.getTradingManner());

		//可用余额提现
		businessVO.setTypeAll(BusTypeAllEnums.EXTRACT.getVal());
		//人工取现
		businessVO.setType(BusTypeEnums.EXTRACT_OFFLINE.getVal());

		//收款人姓名
		businessVO.setReceiptName(bindingVO.getAccountName());
		//收款人账号、卡号
		businessVO.setReceiptAccount(bindingVO.getAccountNo());
		//收款人支行code
		businessVO.setReceiptBank(bindingVO.getBankCode());
		//收款人支行名称
		businessVO.setReceiptBank(bindingVO.getBankName());

		/** 插入提现记录 */
		busService.insert(businessVO);

		/** 插入交易流水 */
		FlowVO flowVO = new FlowVO();

		//收款账号
		flowVO.setReceiptName(bindingVO.getAccountName());
		flowVO.setReceiptAccount(bindingVO.getAccountNo());
		flowVO.setReceiptBankCode(bindingVO.getBankCode());
		flowVO.setReceiptBank(bindingVO.getBankName());

		flowVO.setAmount(req.getAmount());
		flowVO.setActualAmount("0");
		flowVO.setUserId(req.getUserId());
		flowVO.setStatuss(businessVO.getStatuss());
		flowVO.setPlatformId(platformId);
		flowVO.setBusTimestamp(LocalDateTime.now());
		//可用余额提现
		flowVO.setTypeAll(BusTypeAllEnums.EXTRACT.getVal());
		//人工取现
		flowVO.setType(BusTypeEnums.EXTRACT_OFFLINE.getVal());
		flowVO.setCodeId(ran_code);
		flowVO.setCode(String.valueOf(ran_code));
		flowVO.setCodeOther(String.valueOf(ran_code));
		//系统出账
		flowVO.setTradingManner(req.getTradingManner());
		//人工出账
		flowVO.setRemark(BusConstant.EXTRACT_MANUAL);
		flowVO.setDescription(BusConstant.EXTRACT_MANUAL);
		//插入交易流水
		if( !flowService.insertFlow(flowVO) ) {
			//插入流水失败
			throw new BizBusinessException(BaseExceptionType.FLOW_FAIL);
		}

		//插入冻结表数据
		FreezeOrderVO freezeOrderVO = new FreezeOrderVO();
		freezeOrderVO.setId(ran_code);
		freezeOrderVO.setBusId(ran_code);
		freezeOrderVO.setUserId(req.getUserId());
		freezeOrderVO.setBatchNo(String.valueOf(ran_code));
		freezeOrderVO.setFreezeMoney(req.getAmount());
		freezeOrderVO.setFreezeTimestarmp(LocalDateTime.now());
		freezeOrderVO.setType(FreezeOrderTypeEnums.TYPE_EXTRACT.getVal());
		freezeOrderVO.setStatuss(FreezeOrderStatusEnums.FREEZE.getVal());
		freezeOrderVO.setRemarks(BusConstant.FREEZE_EXTRACT);
		if ( !iFreezeOrderService.insert(freezeOrderVO) ) {
			throw new BizBusinessException(BaseExceptionType.DATA_FREEZEORDER_INSERT_FAIL);
		}

		return true;
	}

	/**
	 * 在线提现
	 * 冻结金额 累加， 可用余额 累减
	 * 插入一条记录到交易表
	 * 插入交易流水记录
	 * @param bingdingId 绑定银行卡id
	 * @param amount 提现申请金额
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@Lock("coffey-finance-userId-#{#userId}")
	public boolean insertOnline(Long bingdingId, Long userId, Long platformId, String amount) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		//查询当前人绑定银行账号
		BankBindingVO bindingVO = new BankBindingVO();
		bindingVO.setUserId(userId);
		bindingVO.setPlatformId(platformId);
		bindingVO.setId(bingdingId);
		bindingVO = bindingService.findDetail(bindingVO);
		if ( null == bindingVO) {
			throw new BizBusinessException(BaseExceptionType.BINDING_DATA_NULL);
		}

		/* 查询我的账本 */
		BookVO bookVO = iBookService.findDetail(userId, platformId);
		if(null == bookVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		/** 冻结金额 累加， 可用余额 累减 */
		if(!iBookService.addBalanceExtractReg(bookVO.getId(), platformId, amount)) {
			//可用余额不足， 扣减不成功
			throw new BizBusinessException(BaseExceptionType.DATA_BALANCE_NOTONLINE);
		}

		/** 插入一条在线提现记录 */
		BusinessVO businessVO = new BusinessVO();
		Long ran_code = idService.getNextId();
		businessVO.setCode(ran_code);
		businessVO.setPayCode(ran_code);

		businessVO.setUserId(userId);
		businessVO.setPlatformId(platformId);
		businessVO.setAmount(amount);
		//正在出款
		businessVO.setStatuss(BusStatusEnums.EXTRACT_DOING.getVal());
		//支付状态： 等待支付
		businessVO.setPayStatus(FsPayStatusEnums.PAY_WAIT.getVal());
		businessVO.setPayTimestamp(LocalDateTime.now());
//		//交易方式
//		businessVO.setTradingManner();
//		//支付系统
//		businessVO.setSysKey(payFsService.getIntfaceKey(tradingManner));
		//可用余额提现
		businessVO.setTypeAll(BusTypeAllEnums.EXTRACT.getVal());
		//人工取现
		businessVO.setType(BusTypeEnums.EXTRACT_ONLINE.getVal());

		//收款人姓名
		businessVO.setReceiptName(bindingVO.getAccountName());
		//收款人账号、卡号
		businessVO.setReceiptAccount(bindingVO.getAccountNo());
		//收款人支行code
		businessVO.setReceiptBank(bindingVO.getBankCode());
		//收款人支行名称
		businessVO.setReceiptBank(bindingVO.getBankName());

		/* 插入提现记录 */
		if (!busService.insert(businessVO) ) {
			throw new BizBusinessException(BaseExceptionType.BUS_DATA_BUSINESS_INSERT_NULL);
		}

		/** 插入交易流水 */
		FlowVO flowVO = new FlowVO();

		//收款人姓名
		flowVO.setReceiptName(bindingVO.getAccountName());
		//收款人账号、卡号
		flowVO.setReceiptAccount(bindingVO.getAccountNo());
		//收款人支行code
		flowVO.setReceiptBank(bindingVO.getBankCode());
		//收款人支行名称
		flowVO.setReceiptBank(bindingVO.getBankName());

		flowVO.setAmount(amount);
		flowVO.setUserId(userId);
		flowVO.setStatuss(businessVO.getStatuss());
		flowVO.setPlatformId(platformId);
		flowVO.setBusTimestamp(LocalDateTime.now());
		//可用余额提现
		flowVO.setTypeAll(BusTypeAllEnums.EXTRACT.getVal());
		//人工取现
		flowVO.setType(BusTypeEnums.EXTRACT_OFFLINE.getVal());
		flowVO.setCodeId(ran_code);
		flowVO.setCode(String.valueOf(ran_code));
		flowVO.setCodeOther(String.valueOf(ran_code));
//		//交易方式
//		flowVO.setTradingManner(tradingManner);
//		//支付系统
//		flowVO.setSysKey(payFsService.getIntfaceKey(tradingManner));
		//系统出账
		flowVO.setRemark(BusConstant.EXTRACT_SYS);
		flowVO.setDescription(BusConstant.EXTRACT_SYS);

		//插入交易流水
		if( !flowService.insertFlow(flowVO) ) {
			//插入流水失败
			throw new BizBusinessException(BaseExceptionType.FLOW_FAIL);
		}

		//插入冻结表数据
		FreezeOrderVO freezeOrderVO = new FreezeOrderVO();
		freezeOrderVO.setId(ran_code);
		freezeOrderVO.setBusId(ran_code);
		freezeOrderVO.setUserId(userId);
		freezeOrderVO.setPlatformId(platformId);
		freezeOrderVO.setBatchNo(String.valueOf(ran_code));
		freezeOrderVO.setFreezeMoney(amount);
		freezeOrderVO.setFreezeTimestarmp(LocalDateTime.now());
		freezeOrderVO.setType(FreezeOrderTypeEnums.TYPE_EXTRACT.getVal());
		freezeOrderVO.setStatuss(FreezeOrderStatusEnums.FREEZE.getVal());
		freezeOrderVO.setRemarks(BusConstant.FREEZE_EXTRACT_SYS);
		if ( !iFreezeOrderService.insert(freezeOrderVO) ) {
			throw new BizBusinessException(BaseExceptionType.DATA_FREEZEORDER_INSERT_FAIL);
		}

		return true;
	}

	/**
	 * 人工审核，通过，人工线下转账
	 * 强制取款成功， 冻结金额扣减
	 * 修改交易记录状态   入账成功
	 * 插入流水
	 * 修改冻结状态 == 》 已核销
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateStatusOK(ExtractReviewManualInsertReq req, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		/** 查询取款记录 */
		BusinessVO businessVO = this.findDetailId(req.getId(), platformId);

		this.checkBusinessVO(businessVO);

		/** 查询我的账本 */
		BookVO bookVO = iBookService.findDetail(businessVO.getUserId(), platformId);
		if(null == bookVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		/**  冻结金额累减 */
		if ( !iBookService.addBalanceExtractEnd(bookVO.getId(), platformId, businessVO.getAmount()) ) {
			//冻结金额 不足
			throw new BizBusinessException(BaseExceptionType.DATA_MONEYEXTRACT_NOTONLINE);
		}

		LocalDateTime date = LocalDateTime.now();

		BusinessVO updateBusVO = new BusinessVO();
		updateBusVO.setId(req.getId());
		updateBusVO.setActualAmount(businessVO.getAmount());
		//支付状态： 成功
		updateBusVO.setPayStatus(FsPayStatusEnums.PAY_OK.getVal());
		updateBusVO.setUpdateBy(SecurityUtil.getLoginAccount().getUserName());
		updateBusVO.setUpdateById(SecurityUtil.getLoginAccount().getUserId());
		updateBusVO.setPayTimestamp(date);
		updateBusVO.setUpdateTimestamp(date);
		updateBusVO.setPlatformId(platformId);

		//付款人
		if(!StringUtils.isEmpty(req.getPayAccount())) {
			businessVO.setPayAccount(req.getPayAccount());
		}
		if(!StringUtils.isEmpty(req.getPayBank())) {
			businessVO.setPayBank(req.getPayBank());
		}
		if(!StringUtils.isEmpty(req.getPayName())) {
			businessVO.setPayName(req.getPayName());
			businessVO.setPayBankCode("");
		}

		//支付凭证URL
		if(!StringUtils.isEmpty(req.getFileUrl())) {
			businessVO.setFileUrl(req.getFileUrl());
		}

		/**  修改状态： 入账成功 */
		updateBusVO.setStatuss(BusStatusEnums.EXTRACT_OK.getVal());
		if( !busService.update(updateBusVO) ) {
			throw new BizBusinessException(BaseExceptionType.BOOK_DATA_UPDATE_STATUS);
		}

		/**  插入交易流水记录 */
		FlowVO flowVO = new FlowVO();

		flowVO.setAmount(updateBusVO.getAmount());
		flowVO.setActualAmount(businessVO.getAmount());
		flowVO.setUserId(businessVO.getUserId());
		flowVO.setPlatformId(platformId);
		flowVO.setStatuss(updateBusVO.getStatuss());
		flowVO.setBusTimestamp(date);
		flowVO.setUpdateTimestamp(date);
		flowVO.setCreateTimestamp(date);

		//提现取款冻结金额
		flowVO.setTypeAll(BusTypeAllEnums.EXTRACT_REVIEW.getVal());
		//提现通过
		flowVO.setType(businessVO.getType());
		flowVO.setCodeId(businessVO.getId());
		flowVO.setCode(String.valueOf(businessVO.getId()));
		flowVO.setCodeOther(String.valueOf(businessVO.getId()));

		//人工出账通过，冻结金额扣减且核销
		flowVO.setRemark(BusConstant.EXTRACT_MANUAL_OK);
		flowVO.setDescription(BusConstant.EXTRACT_MANUAL_OK);
		//插入交易流水
		if( !flowService.insertFlow(flowVO) ) {
			//插入流水失败
			throw new BizBusinessException(BaseExceptionType.FAIL);
		}

		//冻结数据修改状态 : 已核销
		this.updateFreezeVerify(req.getId(), platformId, businessVO.getAmount());

		return true;
	}

	/**
	 * 审核不通过
	 * 冻结金额扣减， 可用余额累加
	 * 插入交易流水记录
	 * 修改交易记录状态  拒绝出款
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateStatusFail(Long id, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		/** 1. 查询取款记录 是否存在 */
		BusinessVO businessVO = this.findDetailId(id, platformId);

		this.checkBusinessVO(businessVO);

		/** 查询我的账本 */
		BookVO bookVO = iBookService.findDetail(businessVO.getUserId(), businessVO.getPlatformId());
		if(null == bookVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}

		/** 冻结金额扣减， 可用余额累加 */
		if(!iBookService.addBalanceExtractFail(bookVO.getId(), platformId, businessVO.getAmount())) {
			//冻结金额 扣减不成功
			throw new BizBusinessException(BaseExceptionType.DATA_MONEYEXTRACT_NOTONLINE);
		}

		/**  更新状态 拒绝出款 */
		BusinessVO update_vo = new BusinessVO();
		update_vo.setId(businessVO.getId());
		update_vo.setPlatformId(platformId);
		//拒绝出款
		update_vo.setStatuss(BusStatusEnums.EXTRACT_REFUSE.getVal());
		update_vo.setPayStatus(FsPayStatusEnums.PAY_NO.getVal());
		update_vo.setUpdateById(SecurityUtil.getLoginAccount().getUserId());
		update_vo.setUpdateBy(SecurityUtil.getLoginAccount().getUserName());
		update_vo.setUpdateTimestamp(LocalDateTime.now());
		update_vo.setPayTimestamp(LocalDateTime.now());
		//修改状态， 拒绝出款
		if( !busService.update(update_vo) ) {
			throw new BizBusinessException(BaseExceptionType.BOOK_DATA_UPDATE_STATUS);
		}

		/**  插入交易流水记录 */
		FlowVO flowVO = new FlowVO();
		flowVO.setStatuss(businessVO.getStatuss());
		flowVO.setAmount(businessVO.getAmount());
		flowVO.setActualAmount(businessVO.getAmount());
		flowVO.setUserId(businessVO.getUserId());
		flowVO.setPlatformId(businessVO.getPlatformId());
		flowVO.setBusTimestamp(LocalDateTime.now());
		//系统可用余额累加
		flowVO.setTypeAll(BusTypeAllEnums.EXTRACT.getVal());
		//提现拒绝
		flowVO.setType(BusTypeEnums.EXTRACT_FAIL.getVal());
		flowVO.setCodeId(businessVO.getId());
		flowVO.setCode(String.valueOf(businessVO.getId()));
		flowVO.setCodeOther(String.valueOf(businessVO.getId()));

		//人工出账
		flowVO.setRemark(BusConstant.EXTRACT_MANUAL_FAIL);
		flowVO.setDescription(BusConstant.EXTRACT_MANUAL_FAIL);
		//插入交易流水
		if( !flowService.insertFlow(flowVO) ) {
			//插入流水失败
			throw new BizBusinessException(BaseExceptionType.FAIL);
		}

		this.updateFreezeVerify(id, platformId, businessVO.getAmount());

		return true;
	}

	/**
	 * 审核通过， 系统出账
	 * 	 * 1. 查询提现记录
	 * 	 * 2. 判断提现数据不为空 && 状态为 == 正在出款
	 * 	 * 3. 调用支付系统 系统打款
	 * 	 * 4. 返回数据 成功/失败 ==》 异步接口
	 * 	 * 5.1 异步接口： 回调请求后 ==》扣减冻结金额
	 * 	 * 6. 修改冻结金额表状态 ==> 已核实
	 * @param id
	 * @param payPlatformId  支付平台ID
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateStatusOkSys(Long id, Long platformId, Long payPlatformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		//查询取款记录
		BusinessVO businessVO = this.findDetailId(id, platformId);

		this.checkBusinessVO(businessVO);

		BusinessVO updateBusVO = new BusinessVO();
		updateBusVO.setId(id);
		updateBusVO.setPlatformId(platformId);
		updateBusVO.setUpdateById(SecurityUtil.getLoginAccount().getUserId());
		updateBusVO.setUpdateBy(SecurityUtil.getLoginAccount().getUserName());
		updateBusVO.setUpdateTimestamp(LocalDateTime.now());

		//记录更新人
		if( !busService.update(updateBusVO) ) {
			throw new BizBusinessException(BaseExceptionType.BOOK_DATA_UPDATE_STATUS);
		}

		//微信支付/ 支付宝支付/ 银联支付/ 闪付通在线支付
		if ( null != payPlatformId && payPlatformId.longValue() > 0 ) {

			//提现取款， 不需要付款信息， 从支付系统中直接付款（微信支付，支付宝支付，银行在线支付，闪付通在线支付）
			PayReq payReq = new PayReq();

			payReq.setPayPlatformId(payPlatformId);
			payReq.setSysToken(payToken);

			payReq.setParam(String.valueOf(platformId));
			payReq.setRemarks(businessVO.getPayRemarks());

			String amount = new BigDecimal(businessVO.getAmount()).divide(new BigDecimal(BusConstant.UTILS_DIVISOR)).toString();
			//收款
			payReq.setReceiptAccount(businessVO.getReceiptAccount());
			payReq.setReceiptBank(businessVO.getReceiptBank());
			payReq.setReceiptBankCode(businessVO.getReceiptBankCode());
			payReq.setReceiptName(businessVO.getReceiptName());

			payReq.setPayName("提现系统支付");
			payReq.setPayAccount("提现系统支付");

			payReq.setOrderNo(String.valueOf(businessVO.getCode()));
			payReq.setMoney(amount);

			payReq.setUserId(businessVO.getUserId());
			payReq.setPlatformId(businessVO.getPlatformId());

			PayProductData productReq = new PayProductData();
			productReq.setName(BusConstant.EXTRACT_SYS);
			productReq.setNum(1);
			productReq.setMoeny(businessVO.getAmount());
			payReq.setList(Arrays.asList(productReq));
			payReq.setParam(String.valueOf(businessVO.getCode()));

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
			R<PayResp> r_ret = payFeignClient.pay(payReq);
			PayResp pay_ret_resp = r_ret.getData();
			String msg = r_ret.getMsg();
			if(null != pay_ret_resp) {
				msg = pay_ret_resp.getPayRemarks();
			}
			if( !r_ret.isSuccess() ) {
				throw new BizBusinessException(r_ret.getCode(), "支付系统返回： ==> " + msg);
			}

			RechargeOnlineResp onlineResp = new RechargeOnlineResp();
			onlineResp.setType(pay_ret_resp.getType());
			//返回跳转支付页面
			if (RespTypeEnums.getType(pay_ret_resp.getType()) == RespTypeEnums.PAY) {
				if (StringUtil.isEmpty(pay_ret_resp.getUrl())) {
					throw new BizBusinessException(BaseExceptionType.PAY_URL_NULL);
				}
				onlineResp.setUrl(pay_ret_resp.getUrl());
			}

			//实际支付金额
			this.updateFreezeVerify(id, platformId, pay_ret_resp.getMoney());
		} else {
			// 无支付系统可调用
			this.updateSysCall(id, platformId, businessVO.getAmount(), FsPayStatusEnums.PAY_FAIL);
		}
		return true;
	}

	/**
	 * 提现、取款
	 * 支付系统回调， 处理交易单据 总方法
	 * @param id 交易记录id
	 * @param amount 实际支付金额
	 * @param payStatus 支付返回状态
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateSysCall(Long id, Long platformId, String amount, FsPayStatusEnums payStatus) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		//查询取款记录
		BusinessVO businessVO = this.findDetailId(id, platformId);

		this.checkBusinessVO(businessVO);

		// 支付状态 == 等待
		if(FsPayStatusEnums.getType(businessVO.getPayStatus()) != FsPayStatusEnums.PAY_WAIT) {
			throw new BizBusinessException(BaseExceptionType.DATA_VERIFICATION);
		}

		/**  交易流水记录 VO 参数封装 */
		FlowVO flowVO = new FlowVO();
		flowVO.setUserId(businessVO.getUserId());
		flowVO.setPlatformId(businessVO.getPlatformId());
		//交易方式
		flowVO.setTradingManner(businessVO.getTradingManner());
		//支付系统
		flowVO.setSysKey(businessVO.getSysKey());
		flowVO.setBusTimestamp(LocalDateTime.now());
		flowVO.setAmount(businessVO.getAmount());
		flowVO.setCodeId(id);
		flowVO.setCode(String.valueOf(id));
		flowVO.setCodeOther(String.valueOf(id));
		flowVO.setType(businessVO.getType());
		//提现取款可用金额
		flowVO.setTypeAll(BusTypeAllEnums.EXTRACT.getVal());

		if(payStatus == FsPayStatusEnums.PAY_OK) {
			/**  插入交易流水记录， 冻结金额扣减  */
			this.callOk(id, platformId, amount, flowVO);
		} else if(payStatus == FsPayStatusEnums.PAY_FAIL) {
			/**  插入交易流水记录， 冻结金额扣减， 可用余额累加  */
			this.callFail(id, platformId, amount, flowVO);
		}

		//冻结金额核销
		this.updateFreezeVerify(id, platformId, amount);
	}

	/**
	 * 支付系统返回失败
	 * @param id 交易id
	 * @param amount 实际支付金额
	 * @param flowVO 流水vo
	 */
	private void callFail(Long id, Long platformId, String amount, FlowVO flowVO) {

		/**   冻结金额累减， 可用余额累加  */
		iBookService.addBalanceExtractFail(id, platformId, amount);

		BusinessVO updateBusVO = new BusinessVO();
		updateBusVO.setId(id);
		updateBusVO.setPlatformId(platformId);
		updateBusVO.setActualAmount(amount);
		//支付状态：失败
		updateBusVO.setPayStatus(FsPayStatusEnums.PAY_FAIL.getVal());
		updateBusVO.setPayTimestamp(LocalDateTime.now());
		updateBusVO.setUpdateTimestamp(LocalDateTime.now());

		/**  修改状态： 退回出款 */
		updateBusVO.setStatuss(BusStatusEnums.EXTRACT_RETURN.getVal());
		if( !busService.update(updateBusVO) ) {
			throw new BizBusinessException(BaseExceptionType.BOOK_DATA_UPDATE_STATUS);
		}

		//冻结金额增加
		flowVO.setStatuss(updateBusVO.getStatuss());

		//支付失败==>提现冻结金额减少,可用余额增加
		flowVO.setRemark(BusConstant.EXTRACT_MONEY_FAIL);
		flowVO.setDescription(BusConstant.EXTRACT_MONEY_FAIL);
		flowVO.setPlatformId(platformId);
		//插入交易流水
		if( !flowService.insertFlow(flowVO) ) {
			//插入流水失败
			throw new BizBusinessException(BaseExceptionType.FAIL);
		}
	}

	/**
	 * 支付系统返回成功
	 * @param id 交易id
	 * @param amount 实际支付金额
	 * @param flowVO 流水vo
	 */
	private void callOk(Long id, Long platformId, String amount, FlowVO flowVO) {
		/**   扣减冻结金额  */
		iBookService.addBalanceExtractEnd(id, platformId, amount);

		BusinessVO updateBusVO = new BusinessVO();
		updateBusVO.setId(id);
		updateBusVO.setPlatformId(platformId);
		//实际冻结金额
		updateBusVO.setActualAmount(amount);
		//支付状态： 成功
		updateBusVO.setPayStatus(FsPayStatusEnums.PAY_OK.getVal());
		updateBusVO.setPayTimestamp(LocalDateTime.now());

		/**  修改状态： 入账成功 */
		updateBusVO.setStatuss(BusStatusEnums.EXTRACT_OK.getVal());
		if( !busService.update(updateBusVO) ) {
			throw new BizBusinessException(BaseExceptionType.BOOK_DATA_UPDATE_STATUS);
		}

		/**  插入交易流水记录 */

		//提现冻结金额减少
		flowVO.setRemark(BusConstant.EXTRACT_MONEY);
		flowVO.setDescription(BusConstant.EXTRACT_MONEY);
		flowVO.setPlatformId(platformId);
		flowVO.setStatuss(updateBusVO.getStatuss());

		//插入交易流水
		if( !flowService.insertFlow(flowVO) ) {
			//插入流水失败
			throw new BizBusinessException(BaseExceptionType.FAIL);
		}

	}

	/**
	 * 修改冻结表中冻结状态 ==》 已核销
	 * @param id
	 */
	private void updateFreezeVerify(Long id, Long platformId, String amount) {
		//修改冻结金额状态 ==》 已核销
		FreezeOrderVO freezeOrderVO = new FreezeOrderVO();
		freezeOrderVO.setId(id);
		freezeOrderVO.setPlatformId(platformId);
		freezeOrderVO.setStatuss(FreezeOrderStatusEnums.VERIFY.getVal());
		freezeOrderVO.setVerifyMoney(amount);
		freezeOrderVO.setRemarks(BusConstant.VERIFY_FREEZE);
		iFreezeOrderService.update(freezeOrderVO);
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
		//判断状态 == 正在出款
		if(BusStatusEnums.getType(businessVO.getStatuss()) != BusStatusEnums.EXTRACT_DOING) {
			throw new BizBusinessException(BaseExceptionType.DATA_VERIFICATION);
		}
	}
}
