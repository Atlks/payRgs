package com.platform.top.xiaoyu.run.service.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.vo.SafeRateVO;
import com.platform.top.xiaoyu.run.service.finance.entity.Interest;
import com.platform.top.xiaoyu.run.service.finance.entity.SafeRate;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author coffey
 */
@Mapper
public interface InterestMapper extends BaseMybatisPlusMapper<Interest, Long> {

}
