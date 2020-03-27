package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BookVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.SafeVO;
import com.platform.top.xiaoyu.run.service.finance.entity.Safe;

import java.time.LocalDate;

/**
 * 保险箱
 * @author coffey
 */
public interface ISafeService extends IService<Safe> {

	public Page<SafeVO> findPage(Page<SafeVO> page, SafeVO safeVO);

	/**
	 * 查询保险箱
	 */
	public SafeVO findDetailUserId(Long userId, Long platformId);

	public SafeVO findDetail(SafeVO vo);

	/**
	 * 查询我的保险箱余额
	 * @param userId
	 * @return
	 */
	public BookVO findBalance(Long userId, Long platformId);

	/**
	 * 插入保险箱数据， 设置密码
	 */
	public boolean insert(SafeVO safeVO);

	/**
	 * 重置保险箱密码
	 * @param id
	 * @param pwd
	 * @param platformId
	 * @return
	 */
	public boolean updateReset(Long id, String pwd, Long platformId);

	/**
	 * 修改保险箱密码
	 * @param userId
	 * @param pwd
	 * @param newPwd
	 * @param platformId
	 * @return
	 */
	public boolean updatePwd(Long userId, String pwd, String newPwd, Long platformId);

	/**
	 * 登录保险箱
	 * @param safeVO
	 * @return
	 */
	public boolean sign(SafeVO safeVO);

	/**
	 * 计算保险箱中存储金额的利息
	 */
	public void calcJob();

	/**
	 * 计算 用户当天的利息
	 * @param userId
	 * @param platformId
	 * @param today
	 */
	public void calcUser(Long userId, Long platformId, LocalDate today);

}
