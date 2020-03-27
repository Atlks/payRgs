package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BankBindingVO;
import com.platform.top.xiaoyu.run.service.finance.entity.BankBinding;

import java.util.List;

/**
 * 绑卡
 *
 * @author coffey
 */
public interface IBankBindingService extends IService<BankBinding> {

	public Page<BankBindingVO> findPage(Page<BankBindingVO> page, BankBindingVO vo);

	public List<BankBindingVO> findListALL(BankBindingVO vo);

	public BankBindingVO findDetail(BankBindingVO vo);

	public BankBindingVO findDetailId(Long id, Long platformId);

	public boolean insert(BankBindingVO vo);

	/**
	 * 设置默认提款卡
	 * @param id
	 * @param userId
	 * @return
	 */
	public boolean updateIsDefault(Long id, Long userId, Long platformId);

	/**
	 * 物理删除绑定
	 * @return
	 */
	public boolean del(List<Long> ids, Long platformId);

	/**
	 * 恢复删除
	 * @param id
	 * @return
	 */
	public boolean updateEnable(Long id, Long platformId);

	/**
	 * 作废，逻辑删除
	 * @param id
	 * @return
	 */
	public boolean updateCancal(Long id, Long platformId);

}
