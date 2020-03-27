package com.platform.top.xiaoyu.run.service.pay.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayInfoVO;
import com.platform.top.xiaoyu.run.service.pay.entity.PayInfo;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author coffey
 */
@Mapper
public interface PayInfoMapper extends BaseMybatisPlusMapper<PayInfo, Long> {

	public Page<PayInfoVO> findPage(Page<PayInfoVO> page, @Param("vo") PayInfoVO vo);

	public List<PayInfoVO> findListAll(@Param("vo") PayInfoVO vo);

	public PayInfoVO findDetail(@Param("vo") PayInfoVO vo);


}
