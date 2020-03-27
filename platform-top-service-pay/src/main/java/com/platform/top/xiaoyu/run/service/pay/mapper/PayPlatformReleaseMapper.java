package com.platform.top.xiaoyu.run.service.pay.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayPlatformReleaseVO;
import com.platform.top.xiaoyu.run.service.pay.entity.PayPlatformRelease;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 第三方支付发布平台
 * @author coffey
	*/
@Mapper
public interface PayPlatformReleaseMapper extends BaseMybatisPlusMapper<PayPlatformRelease, Long> {

	public Page<PayPlatformReleaseVO> findPage(Page<PayPlatformReleaseVO> page, @Param("vo") PayPlatformReleaseVO vo);

	public List<PayPlatformReleaseVO> findListAll(@Param("vo") 	PayPlatformReleaseVO vo);

	public PayPlatformReleaseVO findDetail(@Param("vo") PayPlatformReleaseVO vo);

}
