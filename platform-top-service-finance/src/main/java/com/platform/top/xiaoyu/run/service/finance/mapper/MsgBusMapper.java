package com.platform.top.xiaoyu.run.service.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.vo.MsgBusVO;
import com.platform.top.xiaoyu.run.service.finance.entity.MsgBus;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author coffey
 */
@Mapper
public interface MsgBusMapper extends BaseMybatisPlusMapper<MsgBus, Long> {

	public Page<MsgBusVO> findPage(Page page, @Param("vo") MsgBusVO vo);

	public MsgBusVO findDetail(@Param("vo") MsgBusVO vo);

	public List<MsgBusVO> findListAll(@Param("vo") MsgBusVO vo);

}
