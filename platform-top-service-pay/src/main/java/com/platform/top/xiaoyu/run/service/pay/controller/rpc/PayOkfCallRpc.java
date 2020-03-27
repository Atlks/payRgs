package com.platform.top.xiaoyu.run.service.pay.controller.rpc;

import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.constant.PayConstant;
import com.platform.top.xiaoyu.run.service.api.pay.feign.IPayOkfFeignClient;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.okf.OkfCallReq;
import com.platform.top.xiaoyu.run.service.pay.service.okf.IPayApiOkfService;
import com.top.xiaoyu.rearend.boot.controller.TopBaseController;
import com.top.xiaoyu.rearend.component.swagger.controller.RpcController;
import com.top.xiaoyu.rearend.log.annotation.ApiLog;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.lang.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OKF支付平台回调 控制器
 *
 * @author coffey
 */
@RestController
@AllArgsConstructor
@RequestMapping(PayConstant.API_OKF)
@Api(value = "OKF支付平台回调", tags = "OKF支付平台回调")
@RpcController
@Slf4j
public class PayOkfCallRpc extends TopBaseController implements IPayOkfFeignClient {

	@Autowired
	private IPayApiOkfService okfService;

	@GetMapping("/notify")
	@ApiOperation(value = "OKF支付回调通知", notes = "OKF支付回调通知")
	@ApiLog("OKF支付回调通知")
	@Override
	public String notify(@RequestBody @Validated OkfCallReq req) {
		log.info("OKF支付回调通知" + req.toString());
		if(StringUtil.isEmpty(req.getData())) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		//调用 接口
		if( !okfService.notify(req.getData()) ) {
			return "fail";
		}
		return "success";
	}

	@GetMapping("/returnhtml")
	@ApiOperation(value = "OKF支付回调返回HTML", notes = "OKF支付回调返回HTML")
	@ApiLog("OKF支付回调返回HTML")
	@Override
	public void returnhtml(String data) {
		log.info("OKF支付回调返回HTML" + data);
		if(StringUtil.isEmpty(data)) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		//调用 接口
		okfService.returnhtml(data);
	}

}
