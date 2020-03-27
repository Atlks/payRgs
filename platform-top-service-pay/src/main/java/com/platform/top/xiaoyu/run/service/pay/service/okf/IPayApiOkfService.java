package com.platform.top.xiaoyu.run.service.pay.service.okf;

import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.okf.OkfPayResp;


/**
 * 芒果支付系统
 *
 * @author coffey
 */
public interface IPayApiOkfService {

	/**
	 * 调用 okf 支付接口
	 * @param payReq 请求参数
	 * @param money 支付金额 注意 金额除以 100 万
	 * @param payType  微信:1, 支付宝:2, QQ:3，银行卡:4
	 * @return
	 */
	public OkfPayResp payOkfPost(PayReq payReq, String money, String payType);

	/** 通知回调 */
	public boolean notify(String data);

	/** 返回页面 */
	public void returnhtml(String data);

}
