package com.platform.top.xiaoyu.run.service.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountVO;
import com.platform.top.xiaoyu.run.service.finance.entity.ReceiptAccount;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收款账号
 * @author coffey
 */
@Mapper
public interface ReceiptAccountMapper extends BaseMybatisPlusMapper<ReceiptAccount, Long> {

	public Page<ReceiptAccountVO> findPage(Page<ReceiptAccountVO> page, @Param("vo") ReceiptAccountVO vo);

	public ReceiptAccountVO findDetail(@Param("vo") ReceiptAccountVO vo);

	public List<ReceiptAccountVO> findListAll(@Param("vo") ReceiptAccountVO vo);

}
