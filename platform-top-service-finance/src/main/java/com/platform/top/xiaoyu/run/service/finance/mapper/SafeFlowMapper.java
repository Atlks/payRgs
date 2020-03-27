package com.platform.top.xiaoyu.run.service.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.vo.SafeFlowVO;
import com.platform.top.xiaoyu.run.service.finance.entity.SafeFlow;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author coffey
 */
@Mapper
public interface SafeFlowMapper extends BaseMybatisPlusMapper<SafeFlow, Long> {


	public Page<SafeFlowVO> findPage(Page page, @Param("vo") SafeFlowVO safeFlowVO);

	public SafeFlowVO findDetail(@Param("vo") SafeFlowVO vo);

	public List<SafeFlowVO> findListAll(@Param("vo") SafeFlowVO vo);

	/**
	 * 保险箱转入
	 * @param vo
	 * @return
	 */
	public int insertSignIn(SafeFlowVO vo);

}
