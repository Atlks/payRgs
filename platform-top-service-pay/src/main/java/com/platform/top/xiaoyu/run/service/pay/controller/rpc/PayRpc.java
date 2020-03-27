package com.platform.top.xiaoyu.run.service.pay.controller.rpc;

import com.platform.top.xiaoyu.run.service.api.pay.constant.PayConstant;
import com.platform.top.xiaoyu.run.service.api.pay.exception.BasePayExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.feign.IPayFeignClient;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayProductReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.PayResp;
import com.platform.top.xiaoyu.run.service.pay.service.IPayService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.lock.api.annotation.Lock;
import com.top.xiaoyu.rearend.component.swagger.controller.RpcController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 系统支付 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(PayConstant.RPC_PAY)
@Api(value = "系统支付", tags = "系统支付")
@RpcController
public class PayRpc extends TopBaseController implements IPayFeignClient {

	@Autowired
	private IPayService iPayService;

	/**
	 * 调用支付接口， 本公司支付 API
	 */
	@PostMapping("/pay")
	@ApiOperation(value = "支付", notes = "支付")
	@ApiLog("支付")
	@Override
	@Lock("coffey-finance-userId-#{#req.userId}")
	public R<PayResp> pay(@RequestBody @Validated PayReq req) {

		this.checkPay(req.getUserId(), req.getPlatformId(), req.getSysToken(), req.getSignKey(), req.getPayPlatformId(), req.getOrderNo(), req.getMoney(), req.getPayName());

		return R.data(iPayService.pay(req));
	}

	@PostMapping("/paySingle")
	@ApiOperation(value = "支付单个产品", notes = "支付单个产品")
	@ApiLog("支付单个产品")
	@Override
	@Lock("coffey-finance-userId-#{#req.userId}")
	public R<PayResp> paySingle(@RequestBody @Validated PayProductReq req) {

		this.checkPay(req.getUserId(), req.getPlatformId(), req.getSysToken(), req.getSignKey(), req.getPayPlatformId(), req.getOrderNo(), req.getMoney(), req.getPayName());

		return R.data(iPayService.paySingle(req));
	}

	private void checkPay(Long userId, Long platformId, String sysToken, String signKey, Long payPlatformId, String orderNo, String money, String payName) {
		if( null == platformId || platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_PLATFORMID);
		}
		if( null == userId || userId.longValue() <= 0) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_USER_ID);
		}
		//分配的token值
		if( StringUtils.isEmpty(sysToken) ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_SYSTOKEN);
		}
		//签名验证， 内部系统无需验证
		if( StringUtils.isEmpty(signKey) ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_SYSKEY);
		}
		//支付平台ID判断（支付类型： 微信支付、支付宝支付）
		if( null == payPlatformId || payPlatformId.longValue() <= 0 ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_PAYPLATFORMID);
		}
		if( StringUtils.isEmpty(orderNo) ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_ORDERNO);
		}
		if( StringUtils.isEmpty(payName) ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_ACCOUNT);
		}
		if( StringUtils.isEmpty(money) ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_AMOUNT);
		}
		if( new BigDecimal(money).longValue() <= 0) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_AMOUNT);
		}
	}

}
