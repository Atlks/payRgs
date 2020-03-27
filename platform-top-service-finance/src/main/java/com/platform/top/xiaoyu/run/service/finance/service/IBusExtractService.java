package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.enums.FsPayStatusEnums;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.extract.ExtractNotOnlineInsertReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.extract.ExtractReviewManualInsertReq;

import java.util.List;

/**
 * 提现、取款
 *
 * @author coffey
 */
public interface IBusExtractService {

	public Page<BusinessVO> findPage(Page<BusinessVO> page, BusinessVO vo);

	public List<BusinessVO> findListAll(BusinessVO vo);

	public BusinessVO findDetail(BusinessVO vo);

	public BusinessVO findDetailId(Long id, Long platformId);

	/** 人工录入取款记录 */
	public boolean insertNotOnline(ExtractNotOnlineInsertReq req, Long platformId);

	/** 在线提现记录 */
	public boolean insertOnline(Long bingdingId, Long userId, Long platformId, String amount);

	/**
	 * 人工审核 强制成功出款
	 * @return
	 */
	public boolean updateStatusOK(ExtractReviewManualInsertReq req, Long platformId);

	/** 人工审核 拒绝出款 */
	public boolean updateStatusFail(Long id, Long platformId);

	/**
	 * 审核成功， 调用支付系统 系统自动打款
	 * @param id
	 * @param payPlatformId  支付平台ID
	 * @return
	 */
	public boolean updateStatusOkSys(Long id, Long platformId, Long payPlatformId);

	/**
	 * 提现、取款
	 * 支付系统回调， 处理交易单据 总方法
	 * @param id  交易记录id
	 * @param amount 实际支付金额
	 * @param payStatus 支付返回状态
	 */
	public void updateSysCall(Long id, Long platformId, String amount, FsPayStatusEnums payStatus);

}
