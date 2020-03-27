package com.platform.top.xiaoyu.run.service.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.vo.FlowVO;
import com.platform.top.xiaoyu.run.service.finance.entity.Flow;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author coffey
 */
@Mapper
public interface FlowMapper extends BaseMybatisPlusMapper<Flow, Long> {


	public Page<FlowVO> findPage(Page page, @Param("vo") FlowVO flowVO);

	/**
	 * 查询流水明细
	 */
	public FlowVO findFlowDetail(@Param("vo") FlowVO vo);

	/**
	 * 查询最近一次流水
	 * @param vo
	 * @return
	 */
	public FlowVO findLastFlowDetail(@Param("vo") FlowVO vo);

}
