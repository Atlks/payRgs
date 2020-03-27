package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.finance.vo.SafeRateVO;
import com.platform.top.xiaoyu.run.service.finance.entity.SafeRate;

import java.util.List;

/**
 * 保险箱利率配置
 * @author coffey
 */
public interface ISafeRateService extends IService<SafeRate> {

	public Page<SafeRateVO> findPage(Page<SafeRateVO> page, SafeRateVO safeVO);

	public List<SafeRateVO> findListAll(SafeRateVO vo);

	public SafeRateVO findDetail(SafeRateVO vo);

	public boolean insert(SafeRateVO vo);

	public boolean update(SafeRateVO vo);

	/**  物理删除绑定  */
	public boolean del(List<Long> ids, Long platformId);

}
