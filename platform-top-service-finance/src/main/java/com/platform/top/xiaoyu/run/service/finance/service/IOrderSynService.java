package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.finance.vo.OrderSynVO;
import com.platform.top.xiaoyu.run.service.finance.entity.OrderSyn;

/**
 * 打码计算
 * @author coffey
 */
public interface IOrderSynService extends IService<OrderSyn> {

	/**
	 * 查询最新的一条记录
	 * @param userId
	 * @return
	 */
	public OrderSynVO findLastDetail(Long userId, Long platformId);

}
