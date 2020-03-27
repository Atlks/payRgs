package com.platform.top.xiaoyu.run.service.finance.mapper;

import com.platform.top.xiaoyu.run.service.api.finance.vo.OrderSynVO;
import com.platform.top.xiaoyu.run.service.finance.entity.OrderSyn;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author coffey
 */
@Mapper
public interface OrderSynMapper extends BaseMybatisPlusMapper<OrderSyn, Long> {

	/**
	 * 查询最新的一条记录
	 * @param vo
	 * @return
	 */
	public OrderSynVO findLastDetail(@Param("vo") OrderSynVO vo);

}
