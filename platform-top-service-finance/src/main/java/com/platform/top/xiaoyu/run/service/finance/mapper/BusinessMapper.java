package com.platform.top.xiaoyu.run.service.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
import com.platform.top.xiaoyu.run.service.finance.entity.Business;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 交易记录
 * @author coffey
 */
@Mapper
public interface BusinessMapper extends BaseMybatisPlusMapper<Business, Long> {

	public Page<BusinessVO> findPage(Page<BusinessVO> page, @Param("vo") BusinessVO vo);

	public BusinessVO findDetail(@Param("vo") BusinessVO vo);

	public List<BusinessVO> findListAll(@Param("vo") BusinessVO vo);

}
