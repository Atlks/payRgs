package com.platform.top.xiaoyu.run.service.pay.service;

import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayProductReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.PayResp;

import java.time.LocalDateTime;

/**
 * 支付
 *
 * @author coffey
 */
public interface IPayService {

	/** 系统支付接口main  单个产品  */
	public PayResp paySingle(PayProductReq req);

	/** 系统支付接口main */
	public PayResp pay(PayReq req);

	/**
	 * 第三方系统回调main 出口
	 * @param payNo 支付单号
	 * @param amount 实际支付金额
	 * @param reqStr 第三方系统 请求 我方支付系统请求参数
	 * @param attr 自定义返回值, 返回平台ID
	 * @return
	 */
	public boolean callPay(Long payNo, String amount, String reqStr, String attr);

	public String check(Long userId, Long platformId, String sysToken);

	public void check_timeout(Long platformId, LocalDateTime reqDatetime, String sysKey);

}
