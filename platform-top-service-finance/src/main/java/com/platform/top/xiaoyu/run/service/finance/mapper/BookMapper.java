package com.platform.top.xiaoyu.run.service.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BookVO;
import com.platform.top.xiaoyu.run.service.finance.entity.Book;
import com.top.xiaoyu.rearend.component.mybatisplus.mapper.BaseMybatisPlusMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author coffey
 */
@Mapper
public interface BookMapper extends BaseMybatisPlusMapper<Book, Long> {

	public BookVO findDetail(@Param("vo") BookVO vo);

	public List<BookVO> findListAll(@Param("vo") BookVO vo);

	public BookVO findPage(Page<BookVO> page, @Param("vo") BookVO vo);

	/**
	 * 可用余额累减 ，冻结金额累加
	 */
	public int addBalanceExtractReg(@Param("id") Long id, @Param("platformId") Long platformId, @Param("amount") Long amount);

	/**
	 * 冻结金额累减
	 */
	public int addBalanceExtractEnd(@Param("id") Long id, @Param("platformId") Long platformId, @Param("amount") Long amount);

	/**
	 * 登入游戏
	 * 累加冻结金额, 清零 可用余额
	 * @param id
	 * @param balance  可用余额
	 */
	public int addBalanceSignIn(@Param("id") Long id, @Param("platformId") Long platformId, @Param("amount") Long balance);

	/**
	 * 登出游戏
	 * 可用余额累加, 冻结金额扣减
	 * @param id
	 * @param balance
	 * @param balance_freeze
	 * @return
	 */
	public int addBalanceSignOut(@Param("id") Long id, @Param("platformId") Long platformId, @Param("amount") Long balance, @Param("balance_freeze") Long balance_freeze);


	/**
	 * 保险柜转入，可用余额累减, 保险柜金额累加
	 */
	public int addBalanceSafeIn(@Param("id") Long id, @Param("platformId") Long platformId, @Param("amount") Long amount);

	/**
	 * 保险柜转出，可用余额累加, 保险柜金额累减
	 */
	public int addBalanceSafeOut(@Param("id") Long id, @Param("platformId") Long platformId, @Param("amount") Long amount);

	/**
	 * 可用余额累加
	 * @param id
	 * @param amount
	 * @return
	 */
	public int addBalanceRecharge(@Param("id") Long id, @Param("platformId") Long platformId, @Param("amount") Long amount);

	/**
	 * 游戏登出、 提款 审核不通过
	 * 冻结金额累减， 可用余额累加
	 * @param id
	 * @param amount
	 * @return
	 */
	public int addBalanceExtractFail(@Param("id") Long id, @Param("platformId") Long platformId, @Param("amount") Long amount);

}
