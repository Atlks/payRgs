package com.platform.top.xiaoyu.run.service.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BankBindingVO;
import com.platform.top.xiaoyu.run.service.finance.entity.BankBinding;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author coffey
 */
@Mapper
public interface BankBindingMapper extends BaseMybatisPlusMapper<BankBinding, Long> {

	public Page<BankBindingVO> findPage(Page<BankBindingVO> page, @Param("vo") BankBindingVO vo);

	public BankBindingVO findDetail(@Param("vo") BankBindingVO vo);

	public List<BankBindingVO> findListAll(@Param("vo") BankBindingVO vo);

}
