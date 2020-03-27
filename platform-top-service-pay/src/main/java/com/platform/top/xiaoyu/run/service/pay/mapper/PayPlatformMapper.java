package com.platform.top.xiaoyu.run.service.pay.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayPlatformVO;
import com.platform.top.xiaoyu.run.service.pay.entity.PayPlatform;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 第三方支付平台
 * @author coffey
	*/
@Mapper
public interface PayPlatformMapper extends BaseMybatisPlusMapper<PayPlatform, Long> {

	public Page<PayPlatformVO> findPage(Page<PayPlatformVO> page, @Param("vo") PayPlatformVO vo);

	public List<PayPlatformVO> findListAll(@Param("vo") PayPlatformVO vo);

	public PayPlatformVO findDetail(@Param("vo") PayPlatformVO vo);


}
