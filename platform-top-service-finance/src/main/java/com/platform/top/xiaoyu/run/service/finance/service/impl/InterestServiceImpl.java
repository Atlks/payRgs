package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.platform.top.xiaoyu.run.service.finance.entity.Interest;
import com.platform.top.xiaoyu.run.service.finance.entity.SumMoneyFlow;
import com.platform.top.xiaoyu.run.service.finance.mapper.InterestMapper;
import com.platform.top.xiaoyu.run.service.finance.mapper.SumMoneyFlowMapper;
import com.platform.top.xiaoyu.run.service.finance.service.IInterestService;
import com.platform.top.xiaoyu.run.service.finance.service.ISumMoneyFlowService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import org.springframework.stereotype.Service;

/**
 * 利息计算 服务实现类
 *
 * @author coffey
 */
@Service
public class InterestServiceImpl extends AbstractMybatisPlusService<InterestMapper, Interest, Long> implements IInterestService {


}
