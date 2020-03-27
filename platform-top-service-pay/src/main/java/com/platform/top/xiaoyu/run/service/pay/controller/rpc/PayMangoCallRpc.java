package com.platform.top.xiaoyu.run.service.pay.controller.rpc;

import com.platform.top.xiaoyu.run.service.api.pay.constant.PayConstant;
import com.platform.top.xiaoyu.run.service.api.pay.exception.BasePayExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.exception.PayExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.feign.IPayMangoFeignClient;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.mango.MangoCallReq;
import com.platform.top.xiaoyu.run.service.pay.constant.PayServiceConstant;
import com.platform.top.xiaoyu.run.service.pay.service.mango.IPayApiMangoService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.swagger.controller.RpcController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 芒果支付平台回调 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(PayConstant.API_MANGO)
@Api(value = "芒果支付平台回调", tags = "芒果支付平台回调")
@RpcController
public class PayMangoCallRpc extends TopBaseController implements IPayMangoFeignClient {

	@Autowired
	private IPayApiMangoService mangoService;

	@PostMapping("/notify")
	@ApiOperation(value = "支付回调", notes = "支付回调")
	@ApiLog("支付回调")
	@Override
	public String notify(@ModelAttribute @Validated MangoCallReq req) {

		//验证参数 金额字段  订单金额  请求时间
		if ( StringUtils.isEmpty(req.getOrderid()) ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_ORDERNO);
		}
		if( StringUtils.isEmpty(req.getSign()) ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_SIGN);
		}
		if( StringUtils.isEmpty(req.getMemberid()) ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_MEMBERID);
		}
		if( StringUtils.isEmpty(req.getReturncode()) ) {
			throw new BizBusinessException(BasePayExceptionType.PARAM_RETURNCODE);
		}

		try {
			Double amount = Double.parseDouble(req.getAmount());
			if( amount <= 0) {
				throw new BizBusinessException(BasePayExceptionType.PARAM_AMOUNT);
			}
		} catch (Exception ex) {
			throw new BizBusinessException(PayExceptionType.PAY_MANGO_CALL_FAIL_AMOUNT);
		}
		try {
			Double.parseDouble(req.getOriginal_amount());
		} catch (Exception ex) {
			throw new BizBusinessException(PayExceptionType.PAY_MANGO_CALL_FAIL_ORIGINAL_AMOUNT);
		}

		try {
			DateUtil.parse(req.getDatetime(), PayServiceConstant.DATE_PARAM_yyyyMMddHHmmss);
		} catch (Exception ex) {
			throw new BizBusinessException(PayExceptionType.PAY_MANGO_CALL_FAIL_DATETIME);
		}

		//调用 接口
		if( !mangoService.notify(req) ) {
			return "fail";
		}
		return "ok";
	}

//	@PostMapping("/findDetail")
//	@ApiOperation(value = "芒果支付平台查询明细", notes = "芒果支付平台查询明细")
//	@ApiLog("芒果支付平台查询明细")
//	@Override
//	public R<FindPayResp> findDetail(@RequestBody @Validated FindPayReq req) {
//		if(StringUtils.isEmpty(req.getPay_orderid())) {
//			throw new BizBusinessException(BasePayExceptionType.PARAM_PAYNO);
//		}
//		return R.data(mangoService.findPayDetail(req.getPay_orderid()));
//	}

}
