package com.platform.top.xiaoyu.run.service.pay.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.UserLvlVO;
import com.platform.top.xiaoyu.run.service.pay.entity.UserLvl;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户支付等级
 * @author coffey
 */
@Mapper
public interface UserLvlMapper extends BaseMybatisPlusMapper<UserLvl, Long> {

	/**
	 * 分页查询
	 * @param page
	 * @param vo
	 * @return
	 */
	public Page<UserLvlVO> findPage(Page<UserLvlVO> page, @Param("vo") UserLvlVO vo);

	public List<UserLvlVO> findListAll(@Param("vo") UserLvlVO vo);

	public UserLvlVO findDetail(@Param("vo") UserLvlVO vo);

}
