package com.platform.top.xiaoyu.run.service.pay.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.TokenPayVO;
import com.platform.top.xiaoyu.run.service.pay.entity.TokenPay;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 安全认证token
 *
 * @author coffey
 */
@Mapper
public interface TokenPayMapper extends BaseMybatisPlusMapper<TokenPay, Long> {

	public Page<TokenPayVO> findPage(Page<TokenPayVO> page, @Param("vo") TokenPayVO vo);

	public TokenPayVO findDetail(@Param("vo") TokenPayVO vo);

}
