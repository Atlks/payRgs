package com.platform.top.xiaoyu.run.service.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.vo.SafeVO;
import com.platform.top.xiaoyu.run.service.finance.entity.Safe;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author coffey
 */
@Mapper
public interface SafeMapper extends BaseMybatisPlusMapper<Safe, Long> {

	public SafeVO findDetail(@Param("vo") SafeVO vo);

	public Page<SafeVO> findPage(Page page, @Param("vo") SafeVO safeVO);

}
