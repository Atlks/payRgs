package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountValueVO;
import com.platform.top.xiaoyu.run.service.finance.entity.ReceiptAccountValue;

import java.util.List;

public interface IReceiptAccountValueService extends IService<ReceiptAccountValue>  {

	public Page<ReceiptAccountValueVO> findPage(Page<ReceiptAccountValueVO> page, ReceiptAccountValueVO vo);

	public List<ReceiptAccountValueVO> findListALL(ReceiptAccountValueVO vo);

	public ReceiptAccountValueVO findDetail(ReceiptAccountValueVO vo);

	public ReceiptAccountValueVO findDetail(Long id, Long platformId);

	/**
	 * 新增数据
	 * @param toolsId 配置ID
	 * @param platformId 平台ID
	 * @param list 数据 value
	 * @return
	 */
	public boolean insertBatch(Long toolsId, Long platformId, List<Integer> list);

	/**  物理删除绑定  */
	public boolean del(List<Long> ids, Long platformId);

}
