package com.platform.top.xiaoyu.run.service.finance.service;

import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeAllEnums;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeEnums;

/**
 * 新增我的账本余额
 *
 * @author coffey
 */
public interface IBalanceService {

	/**
	 *
	 * @param userId 用户Id
	 * @param platformId  平台Id
	 * @param userName 用户名
	 * @param amount 金额
	 * @param typeAll  交易流水 交易大类
	 * @param type 交易流水 交易类型
	 * @param remarks 备注
	 */
	public void execute(Long userId, Long platformId, String userName, String amount, BusTypeAllEnums typeAll, BusTypeEnums type, String remarks);

}
