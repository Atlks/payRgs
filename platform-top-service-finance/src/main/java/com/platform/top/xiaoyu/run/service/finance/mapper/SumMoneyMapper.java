package com.platform.top.xiaoyu.run.service.finance.mapper;

import com.platform.top.xiaoyu.run.service.finance.entity.SumMoney;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author coffey
 */
@Mapper
public interface SumMoneyMapper extends BaseMybatisPlusMapper<SumMoney, Long> {

    public SumMoney findDetail(@Param("vo") SumMoney vo);

}
