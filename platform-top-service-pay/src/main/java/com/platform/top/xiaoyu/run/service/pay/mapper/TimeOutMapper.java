package com.platform.top.xiaoyu.run.service.pay.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.TimeOutVO;
import com.platform.top.xiaoyu.run.service.pay.entity.TimeOut;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 接口超时配置
 *
 * @author coffey
 */
@Mapper
public interface TimeOutMapper extends BaseMybatisPlusMapper<TimeOut, Long> {

	public Page<TimeOutVO> findPage(Page<TimeOutVO> page, @Param("vo") TimeOutVO vo);

	public Page<TimeOutVO> findPageLvl(Page<TimeOutVO> page, @Param("vo") TimeOutVO vo);

	public TimeOutVO findDetail(@Param("vo") TimeOutVO vo);

	public List<TimeOutVO> findListAll(@Param("vo") TimeOutVO vo);

	public List<TimeOutVO> findListAllLvl(@Param("vo") TimeOutVO vo);

	/** 查询全局配置  */
	int findSecond();

}
