package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountToolsVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.recharge.RechargeTypeResp;
import com.platform.top.xiaoyu.run.service.finance.entity.ReceiptAccountTools;

import java.util.List;

public interface IReceiptAccountToolsService extends IService<ReceiptAccountTools>  {

	public Page<ReceiptAccountToolsVO> findPage(Page<ReceiptAccountToolsVO> page, ReceiptAccountToolsVO vo);

	public List<ReceiptAccountToolsVO> findListALL(ReceiptAccountToolsVO vo);

	public ReceiptAccountToolsVO findDetail(ReceiptAccountToolsVO vo);

	public ReceiptAccountToolsVO findDetailType(Integer type, Long platformId);

	public ReceiptAccountToolsVO findDetail(Long id, Long platformId);

	public boolean insert(ReceiptAccountToolsVO vo);

	/**  物理删除绑定  */
	public boolean del (List<Long> ids, Long platformId);

	/**
	 * 迭代当前配置下所有的设置Value
	 * @param list
	 * @return
	 */
	public List<RechargeTypeResp> findCombox(List<ReceiptAccountToolsVO> list, Long platformId);

}
