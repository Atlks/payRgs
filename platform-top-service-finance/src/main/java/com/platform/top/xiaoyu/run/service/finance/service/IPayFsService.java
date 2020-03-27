package com.platform.top.xiaoyu.run.service.finance.service;

import com.platform.top.xiaoyu.run.service.api.finance.vo.req.callpay.CallPayReq;

/**
 * 支付回调
 * @author coffey
 */
public interface IPayFsService {

	/**  支付回调总入口  */
	public boolean callpay(CallPayReq req);

}
