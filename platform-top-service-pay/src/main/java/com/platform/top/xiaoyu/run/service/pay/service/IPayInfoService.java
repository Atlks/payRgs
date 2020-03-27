package com.platform.top.xiaoyu.run.service.pay.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayInfoVO;

import java.util.List;

/**
 * 现有公司支付系统
 *
 * @author coffey
 */
public interface IPayInfoService {

	/** 分页查询记录列表 */
	public Page<PayInfoVO> findPage(Page<PayInfoVO> page, PayInfoVO vo);

	/** 查询所有记录列表listAll */
	public List<PayInfoVO> findListAll(PayInfoVO vo);

	/**  查询明细  */
	public PayInfoVO findDetailId(Long id, Long platformId);
	/**  查询明细  */
	public PayInfoVO findDetail(PayInfoVO vo);

	public boolean update(PayInfoVO vo);

	public boolean insert(PayInfoVO vo);


}
