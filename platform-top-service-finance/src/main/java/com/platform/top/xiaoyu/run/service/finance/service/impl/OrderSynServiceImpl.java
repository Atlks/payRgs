package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.OrderSynVO;
import com.platform.top.xiaoyu.run.service.finance.entity.OrderSyn;
import com.platform.top.xiaoyu.run.service.finance.mapper.OrderSynMapper;
import com.platform.top.xiaoyu.run.service.finance.service.IOrderSynService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 打码计算 服务实现类
 *
 * @author xiaoyu
 */
@Service
public class OrderSynServiceImpl extends AbstractMybatisPlusService<OrderSynMapper, OrderSyn, Long> implements IOrderSynService {

	@Autowired
	private OrderSynMapper mapper;

	@Override
	public OrderSynVO findLastDetail(Long userId, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findLastDetail(OrderSynVO.builder().userId(userId).platformId(platformId).build());
	}
}
