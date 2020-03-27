package com.platform.top.xiaoyu.run.service.pay.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayFlowVO;
import com.platform.top.xiaoyu.run.service.pay.entity.PayFlow;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author coffey
	*/
@Mapper
public interface PayFlowMapper extends BaseMybatisPlusMapper<PayFlow, Long> {

	public Page<PayFlowVO> findPage(Page<PayFlowVO> page, @Param("vo") PayFlowVO vo);

	public List<PayFlowVO> findListAll(@Param("vo") PayFlowVO vo);

	public PayFlowVO findDetail(@Param("vo") PayFlowVO vo);

}
