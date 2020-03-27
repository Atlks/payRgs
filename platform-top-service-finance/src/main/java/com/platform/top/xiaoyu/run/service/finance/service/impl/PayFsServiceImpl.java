package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeAllEnums;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.callpay.CallPayReq;
import com.platform.top.xiaoyu.run.service.api.pay.utils.PaySignUtil;
import com.platform.top.xiaoyu.run.service.finance.service.IBusExtractService;
import com.platform.top.xiaoyu.run.service.finance.service.IBusRechargeService;
import com.platform.top.xiaoyu.run.service.finance.service.IBusinessService;
import com.platform.top.xiaoyu.run.service.finance.service.IPayFsService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


/**
 * 支付回调 服务实现类
 *
 * @author coffey
 */
@Service
@Slf4j
public class PayFsServiceImpl implements IPayFsService {

	@Value("payToken.pay.token")
	private String token;
	@Value("payToken.pay.sysKey")
	private String sysKey;

	@Autowired
	private IBusinessService businessService;
	@Autowired
	private IBusExtractService extractService;
	@Autowired
	private IBusRechargeService rechargeService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean callpay(CallPayReq req) {
		log.info("财务系统接收到支付系统数据");
		//支付完成回调
		if(token.equals(req.getToken())) {
			throw new BizBusinessException(BaseExceptionType.RPC_CALL_TOKEN_FAIL);
		}
		//查询交易订单是否存在
		BusinessVO businessVO = new BusinessVO();
		businessVO.setId(req.getPayNo());
		businessVO.setPlatformId(Long.parseLong(req.getAttr()));
		businessVO = businessService.findDetail(businessVO);

		if(null == businessVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		//验证签名
		Map<String, String> map = new HashMap<String, String>();
		map.put("platformid", req.getAttr());
		map.put("money", req.getAmount());
		map.put("orderno", req.getOrderNo());
		//密钥
		map.put("syskey", sysKey);
		log.info("财务系统接收到支付系统数据开始验证 reg");
		//签名
		String signKey = PaySignUtil.sign(map);
		if(!signKey.equals(req.getSignKey())) {
			log.info("财务系统接收到支付系统数据，验证签名出错");
			throw new BizBusinessException(BaseExceptionType.PARAM_RPC_SIGN_ENCRYPT);
		}
		log.info("财务系统接收到支付系统数据完成验证 end");
		//提现，取款
		if(BusTypeAllEnums.getType(businessVO.getTypeAll()) == BusTypeAllEnums.EXTRACT) {
			//取款调用总回调方法
			extractService.updateSysCall(businessVO.getId(), businessVO.getPlatformId(), req.getAmount(), req.getStatusPay());
		}

		//充值
		if(BusTypeAllEnums.getType(businessVO.getTypeAll()) == BusTypeAllEnums.RECHARGE) {
			rechargeService.updateSysCall(businessVO.getId(), businessVO.getPlatformId(), req.getAmount(), req.getStatusPay());
		}

		return true;

	}

}
