package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.platform.top.xiaoyu.run.service.finance.entity.OrderSynData;
import com.platform.top.xiaoyu.run.service.finance.mapper.OrderSynDataMapper;
import com.platform.top.xiaoyu.run.service.finance.service.IOrderSynDataService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import org.springframework.stereotype.Service;


/**
 * 打码计算, 原数据表 服务实现类
 *
 * @author xiaoyu
 */
@Service
public class OrderSynDataServiceImpl extends AbstractMybatisPlusService<OrderSynDataMapper, OrderSynData, Long> implements IOrderSynDataService {

}
