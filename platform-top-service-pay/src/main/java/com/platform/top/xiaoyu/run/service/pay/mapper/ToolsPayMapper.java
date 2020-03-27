package com.platform.top.xiaoyu.run.service.pay.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.ToolsPayVO;
import com.platform.top.xiaoyu.run.service.pay.entity.ToolsPay;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户支付接口等级积分配置
 * @author coffey
 */
@Mapper
public interface ToolsPayMapper extends BaseMybatisPlusMapper<ToolsPay, Long> {

	/**
	 * 分页查询
	 * @param page
	 * @param vo
	 * @return
	 */
	public Page<ToolsPayVO> findPage(Page<ToolsPayVO> page, @Param("vo") ToolsPayVO vo);

	public List<ToolsPayVO> findListAll(@Param("vo") ToolsPayVO vo);

	public ToolsPayVO findDetail(@Param("vo") ToolsPayVO vo);

	/** 查询优先级 */
	public ToolsPayVO findPriority(@Param("platformId") Long platformId);

}
