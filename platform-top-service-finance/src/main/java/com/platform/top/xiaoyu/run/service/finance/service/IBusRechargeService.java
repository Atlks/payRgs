package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.enums.FsPayStatusEnums;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.recharge.RechargeInsertOnlineReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.recharge.RechargeOnlineResp;

import java.util.List;

/**
 * 充值、存款
 *
 * @author coffey
 */
public interface IBusRechargeService {

	/**  分页查询充值记录  */
	public Page<BusinessVO> findPage(Page<BusinessVO> page, BusinessVO vo);

	/** 查询明细  */
	public BusinessVO findDetail(BusinessVO vo);

	public BusinessVO findDetailId(Long id, Long platformId);

	/** 查询所有充值记录 */
	public List<BusinessVO> findListAll(BusinessVO vo);

	/**
	 * 在线充值， 新增充值记录
	 * @param req
	 * @return
	 */
	public RechargeOnlineResp insertOnline(RechargeInsertOnlineReq req, Long userId, Long platformId);

	/**  修改充值记录， 人工充值二审通过  */
	public boolean updateOk(Long id, Long platformId);

	/**
	 * 支付系统回调，返回
	 * 修改交易记录 支付状态
	 * 支付成功： 插入交易记录
	 * @param id 交易记录表 主键ID
	 * @param platformId 平台ID
	 * @param amount 实际交易金额
	 * @param payStatus 支付状态： 成功、失败
	 * @return
	 */
	public boolean updateSysCall(Long id, Long platformId, String amount, FsPayStatusEnums payStatus);

	/**
	 * 线下充值
	 * @param userId  用户ID
	 * @param amount  充值金额
	 * @param name 付款人姓名
	 * @param accountId 收款账号id
	 * @return
	 */
	public boolean insertNotOnline(Long userId, Long platformId, String amount, String name, Long accountId);

	/**
	 * 入账 拒绝
	 * @param id
	 * @return
	 */
	public boolean updateFail(Long id, Long platformId);

}
