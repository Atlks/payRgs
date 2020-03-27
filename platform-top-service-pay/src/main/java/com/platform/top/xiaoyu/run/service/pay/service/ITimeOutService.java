package com.platform.top.xiaoyu.run.service.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.TimeOutVO;

import java.util.List;

/**
 * 接口超时配置
 *
 * @author coffey
 */
public interface ITimeOutService {

	public Page<TimeOutVO> findPage(Page<TimeOutVO> page, TimeOutVO vo);

	public Page<TimeOutVO> findPageLvl(Page<TimeOutVO> page, TimeOutVO vo);

	public TimeOutVO findDetail(Long id, Long platformId);

	public TimeOutVO findDetail(TimeOutVO vo);

	public List<TimeOutVO> findListAll(TimeOutVO vo);

	public List<TimeOutVO> findListAllLvl(TimeOutVO vo);

	public boolean insert(TimeOutVO req);

	public boolean update(TimeOutVO req);

	public boolean delBatch(List<Long> ids, Long platformId);

	/**  查询接口超时秒数  */
	public int findTimeOut(String sysKey, Long platformId);

}
