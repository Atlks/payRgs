package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BookVO;
import com.platform.top.xiaoyu.run.service.finance.entity.Book;

import java.util.List;
import java.util.Map;

/**
 * 我的账户
 *
 * @author coffey
 */
public interface IBookService extends IService<Book> {

	/**
	 * 查询我的账本明细 ： 可用余额， 冻结余额， 保险箱金额
	 * @return
	 */
	public BookVO findDetail(Long userId, Long platformId);

	public BookVO findDetail(BookVO vo);

	public List<BookVO> findListAll(BookVO vo);

	/**
	 * 初始化支付提现密码
	 * @param userId
	 * @param platformId
	 * @param pwd
	 * @return
	 */
	public boolean updateInitPwd(Long userId, Long platformId, String pwd);

	/**
	 * 修改支付提现密码
	 * @param userId
	 * @param platformId
	 * @param pwd
	 * @param newPwd
	 * @return
	 */
	public boolean updatePwd(Long userId, Long platformId, String pwd, String newPwd);

	/**  新增我的账本, 查询我的账本 */
	public BookVO inserBook(Long userId, Long platformId, String userName);

	/**
	 *  登入游戏, 可用余额清零， 冻结余额累加
	 *  新增 冻结金额记录 冻结
	 *  新增 交易流水
	 * @return  返回冻结金额
	 */
	public String signIn(Long userId, Long platformId, String userName, String gameName, String gameType);

	/**
	 * 登出游戏
	 * 查询冻结表中存在冻结数据， 并未核销
	 * 可用余额累加， 冻结金额清零
	 * 修改 冻结金额记录 已核销
	 * 新增 交易流水
	 * @param userId 用户ID
	 * @param platformId 平台ID
	 * @param amount 登出金额
	 * @param gameName 游戏名称
	 * @param gameType 游戏ID
	 * @return 返回可用余额
	 */
	public String signOut(Long userId, Long platformId, String amount, String gameName, String gameType);

	/**
	 * 可用余额累减, 保险金额累加
	 */
	public boolean addBalanceSafeIn(Long id, Long platformId, String amount);

	/**
	 * 可用余额累加, 保险金额累减
	 */
	public boolean addBalanceSafeOut(Long id, Long platformId, String amount);

	/**
	 * 充值
	 * 可用余额累加
	 */
	public boolean addBalanceRecharge(Long id, Long platformId, String amount);

	/**
	 * 用户 可用余额累加
	 */
	public boolean addBalanceUserRecharge(Long userId, Long platformId, String amount);

	/**
	 * 提款 申请
	 * 可用余额累减 ， 冻结金额累加
	 */
	public boolean addBalanceExtractReg(Long id, Long platformId, String amount);

	/**
	 * 提款 审核通过
	 * 冻结金额累减
	 */
	public boolean addBalanceExtractEnd(Long id, Long platformId, String amount);

	/**
	 * 提款 审核不通过
	 * 冻结金额累减， 可用余额累加
	 */
	public boolean addBalanceExtractFail(Long id, Long platformId, String amount);


	public String sign(Map<String, String> map);

}
