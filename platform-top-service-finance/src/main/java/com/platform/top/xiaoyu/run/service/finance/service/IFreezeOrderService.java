package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.finance.vo.FreezeOrderVO;
import com.platform.top.xiaoyu.run.service.finance.entity.FreezeOrder;

import java.util.List;

/**
 * 冻结金额
 *
 * @author coffey
 */
public interface IFreezeOrderService extends IService<FreezeOrder> {

	public Page<FreezeOrderVO> findPage(Page<FreezeOrderVO> page, FreezeOrderVO vo);

	public FreezeOrderVO findDetailId(Long userId, Long platformId);

	public FreezeOrderVO findDetail(FreezeOrderVO vo);

	public List<FreezeOrderVO> findListAll(FreezeOrderVO vo);

	public boolean insert(FreezeOrderVO vo);

	public boolean update(FreezeOrderVO vo);

}
