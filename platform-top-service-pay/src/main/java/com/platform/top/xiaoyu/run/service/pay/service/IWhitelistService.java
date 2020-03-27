package com.platform.top.xiaoyu.run.service.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.WhitelistVO;

import java.util.List;

/**
 * 白名单
 *
 * @author coffey
 */
public interface IWhitelistService {

	/**
	 * 查询所有白名单
	 */
	public List<WhitelistVO> findListAll(WhitelistVO vo);

	/**
	 * 分页查询白名单
	 * @return
	 */
	public Page<WhitelistVO> findPage(Page<WhitelistVO> page, WhitelistVO vo);

	public WhitelistVO findDetail(Long id, Long platformId);

	public WhitelistVO findDetail(WhitelistVO vo);

	public boolean insert(WhitelistVO req);

	public boolean update(WhitelistVO req);

	public boolean delBatch(List<Long> ids, Long platformId);

}
