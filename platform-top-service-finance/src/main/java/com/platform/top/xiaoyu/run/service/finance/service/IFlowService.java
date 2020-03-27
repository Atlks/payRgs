package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.finance.vo.FlowVO;
import com.platform.top.xiaoyu.run.service.finance.entity.Flow;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 提现、取款
 *
 * @author coffey
 */
public interface IFlowService extends IService<Flow> {

	/**
	 * 分页查询流水记录
	 */
	public Page<FlowVO> findPage(Page<FlowVO> page, FlowVO vo);

	/**
	 * 查询流水list
	 * @param platformId 平台ID
	 * @param ids 排除id
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<FlowVO> findFlowListAll(Long platformId, List<Long> ids, LocalDateTime beginDate, LocalDateTime endDate);

	/**
	 * 查询流水 计总
	 * @param platformId 平台ID
	 * @param ids 排除id
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public Integer findFlowListCount(Long platformId, List<Long> ids, LocalDateTime beginDate, LocalDateTime endDate);

	/**
	 * 查询最近一次流水
	 * @param vo
	 * @return
	 */
	public FlowVO findLastFlowDetail(FlowVO vo);

	public boolean insertFlow(FlowVO vo);

//	public boolean update(FlowVO vo);

}
