package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.enums.SumMoneyTypeEnums;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.mybook.BookApiReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.book.BookCountResp;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.book.BookFlowApiResp;

/**
 * 账户资金
 *
 * @author coffey
 */
public interface IBookApiService {

	/**
	 * 统计账本数据, 充值， 提现总次数
	 * @param userId 用户ID
	 * @param platformId 平台ID
	 * @param enums 当前枚举
	 * @return
	 */
	public void saveSumNumber(Long userId, Long platformId, SumMoneyTypeEnums enums);

	/**
	 * 分页查询账本流水记录
	 * @param req
	 * @param userId
	 * @param platformId
	 * @return
	 */
	public Page<BookFlowApiResp> findPage(BookApiReq req, Long userId, Long platformId);

	/**
	 * 统计 计总  充值、提现、优惠、打码返水
	 * @param userId
	 * @param platformId
	 * @return
	 */
	public BookCountResp getData(Long userId, Long platformId);

}
