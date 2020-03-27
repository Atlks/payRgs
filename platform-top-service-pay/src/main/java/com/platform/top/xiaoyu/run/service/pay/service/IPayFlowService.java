package com.platform.top.xiaoyu.run.service.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayFlowVO;

import java.util.List;

/**
 * 接口超时配置
 *
 * @author coffey
 */
public interface IPayFlowService {

	public Page<PayFlowVO> findPage(Page<PayFlowVO> page, PayFlowVO vo);

	public List<PayFlowVO> findListAll(PayFlowVO vo);

	public PayFlowVO findDetail(Long id, Long platformId);

	public PayFlowVO findDetail(PayFlowVO vo);

	public boolean insert(PayFlowVO vo);

	public boolean update(PayFlowVO vo);

}
