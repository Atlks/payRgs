package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.finance.vo.SafeFlowVO;
import com.platform.top.xiaoyu.run.service.finance.entity.SafeFlow;

import java.util.List;

/**
 * 保险箱流水
 * @author coffey
 */
public interface ISafeFlowService extends IService<SafeFlow> {

	public Page<SafeFlowVO> findPage(Page<SafeFlowVO> page, SafeFlowVO vo);

	public List<SafeFlowVO> findListAll(SafeFlowVO vo);

	public SafeFlowVO findDetail(SafeFlowVO vo);

	public SafeFlowVO findDetailId(Long id, Long platformId);

	/** 转入 */
	public boolean insertFlowIn(SafeFlowVO safeFlowVO);

	/** 转出 */
	public boolean insertFlowOut(SafeFlowVO safeFlowVO);

}
