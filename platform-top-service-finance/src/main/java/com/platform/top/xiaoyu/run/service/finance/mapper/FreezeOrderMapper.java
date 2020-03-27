package com.platform.top.xiaoyu.run.service.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.vo.FreezeOrderVO;
import com.platform.top.xiaoyu.run.service.finance.entity.FreezeOrder;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author coffey
 */
@Mapper
public interface FreezeOrderMapper extends BaseMybatisPlusMapper<FreezeOrder, Long> {

	public Page<FreezeOrderVO> findPage(Page page, @Param("vo") FreezeOrderVO safeVO);

	public FreezeOrderVO findDetail(@Param("vo") FreezeOrderVO vo);

	public List<FreezeOrderVO> findListAll(@Param("vo") FreezeOrderVO vo);

}
