package com.platform.top.xiaoyu.run.service.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountValueVO;
import com.platform.top.xiaoyu.run.service.finance.entity.ReceiptAccountValue;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收款账号 值列表
 * @author coffey
 */
@Mapper
public interface ReceiptAccountValueMapper extends BaseMybatisPlusMapper<ReceiptAccountValue, Long> {

	public Page<ReceiptAccountValueVO> findPage (Page<ReceiptAccountValueVO> page, @Param("vo") ReceiptAccountValueVO vo);

	public ReceiptAccountValueVO findDetail (@Param("vo") ReceiptAccountValueVO vo);

	public List<ReceiptAccountValueVO> findListAll (@Param("vo") ReceiptAccountValueVO vo);

}
