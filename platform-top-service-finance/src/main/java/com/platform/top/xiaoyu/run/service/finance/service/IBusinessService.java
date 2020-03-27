package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
import com.platform.top.xiaoyu.run.service.finance.entity.Business;

import java.util.List;

/**
 * 交易记录
 *
 * @author coffey
 */
public interface IBusinessService extends IService<Business> {

	/**  分页查询充值记录  */
	public Page<BusinessVO> findPage(Page<BusinessVO> page, BusinessVO vo);

	/** 查询明细  */
	public BusinessVO findDetail(BusinessVO vo);

	public BusinessVO findDetailId(Long id, Long platformId);

	/** 查询所有 */
	public List<BusinessVO> findListAll(BusinessVO vo);

	/**  新增充值记录  */
	public boolean insert(BusinessVO vo);

	/**  更新充值状态  */
	public boolean update(BusinessVO vo);

}
