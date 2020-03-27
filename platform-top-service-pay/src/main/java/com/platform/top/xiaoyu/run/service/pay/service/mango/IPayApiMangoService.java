package com.platform.top.xiaoyu.run.service.pay.service.mango;

import com.platform.top.xiaoyu.run.service.api.pay.vo.req.mango.MangoCallReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.mango.FindPayResp;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.mango.MangoPayResp;

import java.util.List;
import java.util.Map;


/**
 * 芒果支付系统
 *
 * @author coffey
 */
public interface IPayApiMangoService {

	/**
	 * 支付接口，  芒果支付系统对接
	 * @param payReq 请求参数
	 * @param money 支付金额 注意需要 传 小数位 保留2位的数字并且 金额除以100万
	 * @param payType //		963 银联扫码/云闪付
	 * //		972 支付宝
	 * //		975 支付宝转卡 扫码方式
	 * //		985 微信转卡
	 * //		986 zfb转卡 跳转页面
	 * @return
	 */
	public MangoPayResp payMangoPost(PayReq payReq, String money, String payType);

	/** 支付接口， 通知回调 */
	public boolean notify(MangoCallReq payCallReq);

	/** 代付接口， 芒果支付系统对接 */
	public void payMangoDfPost();

	/** 代付接口， 通知回调 */
	public void payMangoDfPostCall();

	/** 查询当前订单的支付信息 */
	public FindPayResp findPayDetail(String pay_orderid);

	/** 查询当前订单代付信息 */
	public void findDfPayDetail();

	public List<Map<Integer, String>> findBankCode();


}
