package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountVO;
import com.platform.top.xiaoyu.run.service.finance.entity.ReceiptAccount;

import java.util.List;

public interface IReceiptAccountService extends IService<ReceiptAccount>  {

	public Page<ReceiptAccountVO> findPage(Page<ReceiptAccountVO> page, ReceiptAccountVO vo);

	public List<ReceiptAccountVO> findListALL(ReceiptAccountVO vo);

	public ReceiptAccountVO findDetail(ReceiptAccountVO vo);

	public ReceiptAccountVO findDetail(Long id, Long platformId);

	public boolean insert(ReceiptAccountVO vo);

	/** 设置默认 */
	public boolean updateIsDefault(Long id, Long userId, Long platformId);

	/**  物理删除绑定  */
	public boolean del(List<Long> ids, Long platformId);

	/** 取消绑定，逻辑删除 */
	public boolean updateStatus(Long id, Long platformId);

}
