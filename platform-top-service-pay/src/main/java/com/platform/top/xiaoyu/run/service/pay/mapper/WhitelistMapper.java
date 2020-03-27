package com.platform.top.xiaoyu.run.service.pay.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.WhitelistVO;
import com.platform.top.xiaoyu.run.service.pay.entity.Whitelist;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 白名单
 * @author coffey
 */
@Mapper
public interface WhitelistMapper extends BaseMybatisPlusMapper<Whitelist, Long> {

	/**
	 * 分页查询
	 * @param page
	 * @param vo
	 * @return
	 */
	public Page<WhitelistVO> findPage(Page<WhitelistVO> page, @Param("vo") WhitelistVO vo);

	public List<WhitelistVO> findListAll(@Param("vo") WhitelistVO vo);

	public WhitelistVO findDetail(@Param("vo") WhitelistVO vo);

}
