package com.platform.top.xiaoyu.run.service.finance.controller.rpc;

import com.platform.top.xiaoyu.run.service.api.finance.constant.TopBookConstant;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.feign.IPayFsFeignClient;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.callpay.CallPayReq;
import com.platform.top.xiaoyu.run.service.finance.service.IPayFsService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.lock.api.annotation.Lock;
import com.top.xiaoyu.rearend.component.swagger.controller.RpcController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.lang.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 回调支付请求财务 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(TopBookConstant.RPC_PAY)
@Api(value = "回调支付请求财务", tags = "回调支付请求财务")
@RpcController
public class CallPayRpc extends TopBaseController implements IPayFsFeignClient {

	@Autowired
	private IPayFsService iPayFsService;

	@PostMapping("/callPay")
	@ApiOperation(value = "回调支付请求财务", notes = "回调支付请求财务")
	@ApiLog("回调支付请求财务")
	@Override
	@Lock("coffey-finance-userId-#{#req.userId}")
	public R callPay(CallPayReq req) {

		if(null == req.getUserId() || req.getUserId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_USER_ID);
		}
		if(null == req.getPlatformId() || req.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		if(StringUtil.isEmpty(req.getAmount())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
		}
		//转成金额
		BigDecimal amount = new BigDecimal(req.getAmount());
		if( null == amount || amount.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
		}

		if(StringUtils.isEmpty(req.getOrderNo())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_ORDERNO);
		}
		if(null == req.getPayNo() || req.getPayNo().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAYNO);
		}
		if( null == req.getStatusPay() ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_STATUSPAY);
		}
		if(null == req.getPayDatetime() ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PAYDATETIME);
		}
		if(StringUtil.isEmpty(req.getSignKey())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_RPC_SIGN_ENCRYPT);
		}
		if(StringUtil.isEmpty(req.getToken())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_RPC_TOKEN_NULL);
		}
		//暂时用于, 平台ID
		if(StringUtil.isEmpty(req.getAttr())) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		if ( Long.parseLong(req.getAttr()) <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		return R.data(iPayFsService.callpay(req));
	}
}
