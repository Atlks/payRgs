package com.platform.top.xiaoyu.run.service.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountToolsVO;
import com.platform.top.xiaoyu.run.service.finance.entity.ReceiptAccountTools;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收款账号 配置表
 * @author coffey
 */
@Mapper
public interface ReceiptAccountToolsMapper extends BaseMybatisPlusMapper<ReceiptAccountTools, Long> {

	public Page<ReceiptAccountToolsVO> findPage (Page<ReceiptAccountToolsVO> page, @Param("vo") ReceiptAccountToolsVO vo);

	public ReceiptAccountToolsVO findDetail (@Param("vo") ReceiptAccountToolsVO vo);

	public List<ReceiptAccountToolsVO> findListAll (@Param("vo") ReceiptAccountToolsVO vo);

}
