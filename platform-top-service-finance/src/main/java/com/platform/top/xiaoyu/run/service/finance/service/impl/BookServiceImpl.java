package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.top.xiaoyu.run.service.api.finance.enums.*;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BookVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.FlowVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.FreezeOrderVO;
import com.platform.top.xiaoyu.run.service.api.pay.utils.PaySignUtil;
import com.platform.top.xiaoyu.run.service.finance.constant.BusConstant;
import com.platform.top.xiaoyu.run.service.finance.entity.Book;
import com.platform.top.xiaoyu.run.service.finance.financeutils.StringIsChinaStrUtil;
import com.platform.top.xiaoyu.run.service.finance.mapper.BookMapper;
import com.platform.top.xiaoyu.run.service.finance.service.IBookService;
import com.platform.top.xiaoyu.run.service.finance.service.IFlowService;
import com.platform.top.xiaoyu.run.service.finance.service.IFreezeOrderService;
import com.top.xiaoyu.rearend.component.lock.api.annotation.Lock;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import com.top.xiaoyu.rearend.tool.util.digest.md5.MD5Util;
import com.top.xiaoyu.rearend.tool.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * 我的账本 服务实现类
 *
 * @author coffey
 */
@Service
@Slf4j
public class BookServiceImpl extends AbstractMybatisPlusService<BookMapper, Book, Long> implements IBookService {

	@Value("${pwd.mybook.pwdKey}")
	private String payPwdKey;

	@Autowired
	private BookMapper bookMapper;
	@Autowired
	private IFlowService flowService;
	@Autowired
	private IFreezeOrderService iFreezeOrderService;

	@Override
	public BookVO findDetail(Long userId, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return this.findDetail(BookVO.builder().userId(userId).platformId(platformId).build());
	}


	@Override
	public BookVO findDetail(BookVO vo) {
		if( null != vo && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return bookMapper.findDetail(vo);
	}

	@Override
	public List<BookVO> findListAll(BookVO vo) {
		if( null != vo && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return bookMapper.findListAll(vo);
	}

	/**
	 * 初始化密码
	 * @param userId
	 * @param platformId
	 * @param pwd
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateInitPwd(Long userId, Long platformId, String pwd) {
		BookVO bookVO = this.findDetail(userId, platformId);
		if ( null == bookVO ) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		return updatePwdVoid(bookVO.getId(), pwd);
	}

	/**
	 * 修改支付提现密码
	 * @param userId
	 * @param platformId
	 * @param pwd
	 * @param newPwd
	 * @return
	 */
	@Override
	public boolean updatePwd(Long userId, Long platformId, String pwd, String newPwd) {
		BookVO bookVO = this.findDetail(userId, platformId);
		if ( null != bookVO ) {
			//提现密码 与 当前密码不匹配
			if (!bookVO.getExtractPwd().equals(MD5Util.string2MD5HexUpper(payPwdKey + pwd))) {
				throw new BizBusinessException(BaseExceptionType.PARAM_OLDPWD_FAIL);
			}
		} else {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		return updatePwdVoid(bookVO.getId(), pwd);
	}

	/**
	 * 修改密码
	 * @param id
	 * @param pwd
	 * @return
	 */
	private boolean updatePwdVoid(Long id, String pwd) {

		//判断密码不能输入中文
		if ( StringIsChinaStrUtil.isChinese(pwd) ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PWD_CHINA);
		}

		QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<Book>();
		Book entity = new Book();
		entity.setExtractPwd(MD5Util.string2MD5HexUpper(payPwdKey + pwd));

		return this.update(entity, bookQueryWrapper.eq(Book.PK_ID, id));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BookVO inserBook(Long userId, Long platformId, String userName) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		BookVO bookVO = this.findDetail(BookVO.builder().userId(userId).platformId(platformId).build());
		if( null !=  bookVO) {
			return bookVO;
		}
		if(StringUtil.isEmpty(userName)) {
			throw new BizBusinessException(BaseExceptionType.PARAM_USER_NAME_NULL);
		}

		bookVO = new BookVO();
		bookVO.setId(idService.getNextId());
		bookVO.setUserId(userId);
		bookVO.setPlatformId(platformId);
		bookVO.setUserName(userName);
		bookVO.setBalance("0");
		bookVO.setBalanceNumber("0");
		bookVO.setBalanceSafe("0");
		bookVO.setMoneyExtract("0");
		bookVO.setCreateTimestamp(LocalDateTime.now());

		//我的账本新增记录
		if( bookMapper.insert(BeanCopyUtils.copyBean(bookVO, Book.class)) <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.BOOK_DATA_INSERT_NULL);
		}

		return bookVO;
	}

	/**
	 * 可用余额清零， 登入上分冻结金额累加
	 * @param userId
	 * @param platformId
	 * @param userName
	 * @param gameName
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String signIn(Long userId, Long platformId, String userName, String gameName, String gameType) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		/**  返回可用余额 */
		String ret_balance = "0";

		/** 查询我的账号， 无账本数据新增数据行 */
		BookVO bookVO = this.findDetail(userId, platformId);
		if (null == bookVO) {
			bookVO = this.inserBook(userId, platformId, userName);
		}

		//当前可用余额是否有小数
		int index_ = bookVO.getBalance().indexOf(".");
		//当前可用余额
		ret_balance =  bookVO.getBalance();
		if(index_ > 0) {
			//可用余额取整数
			ret_balance =  bookVO.getBalance().substring(0, index_);
		}
		if(Long.parseLong(ret_balance) <= 0 ) {
			return ret_balance;
		}

		log.info("登入游戏，可用余额清零， 冻结上分,余额累加");
		//可用余额清零， 冻结上分,余额累加
		if( bookMapper.addBalanceSignIn(bookVO.getId(), bookVO.getPlatformId(), new BigDecimal(ret_balance).longValue()) <= 0 ) {
			//上分数据异常
			throw new BizBusinessException(BaseExceptionType.SIGNIN_FAIL);
		}

		/**
		 * 登入游戏
		 * 上分插入交易流水： 系统上分, 余额清零，记录流水表
		 */
		LocalDateTime localDateTime = LocalDateTime.now();

		FlowVO flowVO = new FlowVO();

		flowVO.setUserId(userId);
		flowVO.setPlatformId(platformId);
		flowVO.setActualAmount(ret_balance);
		flowVO.setAmount(ret_balance);
		flowVO.setBusTimestamp(localDateTime);
		//登入 大类 可用金额扣减
		flowVO.setTypeAll(BusTypeAllEnums.BALANCE_LESS.getValue());
		//登入游戏
		flowVO.setType(BusTypeEnums.SYS_SIGNIN.getVal());
		//系统出账
		flowVO.setTradingManner(BusTradingMannerEnums.BUS_TRA_ADMIN_OUT.getVal());
		//登入游戏
		flowVO.setDescription(BusConstant.SIGNIN);
		flowVO.setRemark(gameName);
		flowVO.setGameName(gameName);
		flowVO.setGameType(gameType);
		flowVO.setCreateTimestamp(localDateTime);

		//插入流水
		if( !flowService.insertFlow(flowVO) ) {
			throw new BizBusinessException(BaseExceptionType.FLOW_FAIL);
		}

		Long ran_code = idService.getNextId();
		//插入冻结表数据
		FreezeOrderVO freezeOrderVO = new FreezeOrderVO();
		freezeOrderVO.setUserId(userId);
		freezeOrderVO.setPlatformId(platformId);
		freezeOrderVO.setCreateTimestamp(LocalDateTime.now());
		freezeOrderVO.setId(ran_code);
		freezeOrderVO.setBusId(ran_code);
		freezeOrderVO.setGameName(gameName);
		freezeOrderVO.setGameType(gameType);
		freezeOrderVO.setBatchNo(String.valueOf(ran_code));
		freezeOrderVO.setFreezeMoney(ret_balance);
		freezeOrderVO.setFreezeTimestarmp(LocalDateTime.now());
		freezeOrderVO.setType(FreezeOrderTypeEnums.TYPE_SIGNIN.getVal());
		freezeOrderVO.setStatuss(FreezeOrderStatusEnums.FREEZE.getVal());
		freezeOrderVO.setRemarks(BusConstant.FREEZE_SIGNIN);
		if ( !iFreezeOrderService.insert(freezeOrderVO) ) {
			throw new BizBusinessException(BaseExceptionType.DATA_FREEZEORDER_INSERT_FAIL);
		}

		return ret_balance;
	}

	/**
	 * 登出 余额累加
	 * @param userId 用户ID
	 * @param platformId 平台ID
	 * @param amount 登出金额
	 * @param gameName 游戏名称
	 * @param gameType 游戏ID
	 * @return 返回可用余额
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String signOut(Long userId, Long platformId, String amount, String gameName, String gameType) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		/** 查询我的账号， 无账本数据新增数据行 */
		BookVO bookVO = this.findDetail(userId, platformId);
		if (null == bookVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		log.info("signOut 登出游戏");

		//查询用户最新的一条冻结记录
		//判断登入冻结数据是否存在
		FreezeOrderVO find_freezeVO = new FreezeOrderVO();
		find_freezeVO.setUserId(userId);
		find_freezeVO.setPlatformId(platformId);
		//游戏ID
		find_freezeVO.setGameType(gameType);
		//登入游戏
		find_freezeVO.setType(FreezeOrderTypeEnums.TYPE_SIGNIN.getVal());
		//冻结
		find_freezeVO.setStatuss(FreezeOrderStatusEnums.FREEZE.getVal());

		List<FreezeOrderVO> freezeOrderVOS = iFreezeOrderService.findListAll(find_freezeVO);

		//冻结表中的冻结金额
		long amountFreezeCount = 0;

		if(!CollectionUtils.isEmpty(freezeOrderVOS)) {
			log.info("signOut 游戏金额核销");
			//核销金额
			long varify_money = new BigDecimal(amount).longValue();

			for ( FreezeOrderVO orderVO : freezeOrderVOS ) {
				//冻结余额表状态 : 已核销
				FreezeOrderVO update_freezeVO = new FreezeOrderVO();
				update_freezeVO.setId(orderVO.getId());
				update_freezeVO.setPlatformId(platformId);
				update_freezeVO.setVerifyMoney(varify_money+"");
				update_freezeVO.setStatuss(FreezeOrderStatusEnums.VERIFY.getVal());
				update_freezeVO.setRemarks(BusConstant.SIGNOUT);
				update_freezeVO.setUpdateTimestamp(LocalDateTime.now());

				if ( !iFreezeOrderService.update(update_freezeVO) ) {
					throw new BizBusinessException(BaseExceptionType.DATA_FREEZEORDER_UPDATE_FAIL);
				}
				varify_money -= new BigDecimal(orderVO.getFreezeMoney()).longValue();
				amountFreezeCount += new BigDecimal(orderVO.getFreezeMoney()).longValue();
			}
		} else {
			//未查询到冻结金额， 强制登出，从流水中把冻结金额还原

			log.info("冻结表中， 未查询到登入冻结数据。");

			//从 流水表中去取最近的一条 【登入游戏】 记录
			FlowVO find_flowVO = new FlowVO();
			find_flowVO.setGameType(gameType);
			//登入 大类 可用金额扣减
			find_flowVO.setTypeAll(BusTypeAllEnums.BALANCE_LESS.getValue());
			//登入游戏
			find_flowVO.setType(BusTypeEnums.SYS_SIGNIN.getVal());
			//系统出账
			find_flowVO.setTradingManner(BusTradingMannerEnums.BUS_TRA_ADMIN_OUT.getVal());

			find_flowVO = flowService.findLastFlowDetail(find_flowVO);

			if(null == find_flowVO) {
				log.info("流水表中， 未查询到登入冻结数据。");
				throw new BizBusinessException(BaseExceptionType.FLOW_DATA_FAIL);
			}
			amountFreezeCount = new BigDecimal(bookVO.getBalanceNumber()).longValue();
		}

		log.info("登出游戏可用余额累加， 冻结金额扣减");
		//可用余额累加, 冻结金额扣减
		if ( bookMapper.addBalanceSignOut(bookVO.getId(), bookVO.getPlatformId(), new BigDecimal(amount).longValue(), amountFreezeCount) <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.DATA_BALANCE);
		}

		//插入流水
		FlowVO flowVO = new FlowVO();

		flowVO.setGameName(find_freezeVO.getGameName());
		flowVO.setGameType(gameType);
		flowVO.setUserId(userId);
		flowVO.setPlatformId(platformId);
		flowVO.setActualAmount(amount);
		flowVO.setAmount(amount);
		flowVO.setBusTimestamp(LocalDateTime.now());
		flowVO.setCreateTimestamp(LocalDateTime.now());

		//登出 大类 可用金额累加
		flowVO.setTypeAll(BusTypeAllEnums.BALANCE_CALC.getValue());
		//登出游戏
		flowVO.setType(BusTypeEnums.SYS_SIGNOUT.getVal());
		//系统出账
		flowVO.setTradingManner(BusTradingMannerEnums.BUS_TRA_ADMIN_IN.getVal());
		//登出游戏
		flowVO.setDescription(BusConstant.SIGNOUT);
		flowVO.setRemark(gameName);
		flowVO.setGameName(gameName);
		flowVO.setGameType(gameType);
		if( !flowService.insertFlow(flowVO) ) {
			throw new BizBusinessException(BaseExceptionType.FLOW_FAIL);
		}

		//返回可用余额
		return amount;
	}

	/**
	 * 保险柜转入，可用余额累减, 保险柜金额累加
	 * @param id
	 * @param amount
	 * @return
	 */
	@Override
	@Lock("coffey-finance-Id-#{#id}")
	@Transactional(rollbackFor = Exception.class)
	public boolean addBalanceSafeIn(Long id, Long platformId, String amount) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		int count = bookMapper.addBalanceSafeIn(id, platformId, new BigDecimal(amount).longValue());
		if(count > 0 ) {
			return true;
		}
		return false;
	}

	/**
	 * 保险柜转出，可用余额累加, 保险柜金额累减
	 * @param id
	 * @param amount
	 * @return
	 */
	@Override
	@Lock("coffey-finance-Id-#{#id}")
	@Transactional(rollbackFor = Exception.class)
	public boolean addBalanceSafeOut(Long id, Long platformId, String amount) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		int count = bookMapper.addBalanceSafeOut(id, platformId, new BigDecimal(amount).longValue());
		if(count > 0 ) {
			return true;
		}
		return false;
	}

	/**
	 * 可用余额累加
	 * @param id
	 * @param amount
	 * @return
	 */
	@Override
	@Lock("coffey-finance-Id-#{#id}")
	@Transactional(rollbackFor = Exception.class)
	public boolean addBalanceRecharge(Long id, Long platformId, String amount) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		int count = bookMapper.addBalanceRecharge(id, platformId, new BigDecimal(amount).longValue());
		if(count > 0 ) {
			return true;
		}
		return false;
	}

	/**
	 * 可用余额 累加
	 * @param userId
	 * @param platformId
	 * @param amount
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addBalanceUserRecharge(Long userId, Long platformId, String amount) {
		BookVO bookVO = this.findDetail(userId, platformId);
		if(null == bookVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		return this.addBalanceRecharge(bookVO.getId(), platformId, amount);
	}

	/**
	 * 提款 申请
	 * 可用余额累减 ，冻结金额累加
	 * @param id
	 * @param amount
	 * @return
	 */
	@Override
	@Lock("coffey-finance-Id-#{#id}")
	@Transactional(rollbackFor = Exception.class)
	public boolean addBalanceExtractReg(Long id, Long platformId, String amount) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		if(StringUtil.isEmpty(amount) || new BigDecimal(amount).longValue() <= 0) {
			throw new BizBusinessException(BaseExceptionType.PARAM_AMOUNT);
		}
		int count = bookMapper.addBalanceExtractReg(id, platformId, new BigDecimal(amount).longValue());
		if( count > 0 ) {
			return true;
		}
		return false;
	}

	/**
	 * 冻结金额累减
	 * @param id
	 * @param amount
	 * @return
	 */
	@Override
	@Lock("coffey-finance-Id-#{#id}")
	@Transactional(rollbackFor = Exception.class)
	public boolean addBalanceExtractEnd(Long id, Long platformId, String amount) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		int count = bookMapper.addBalanceExtractEnd(id, platformId, new BigDecimal(amount).longValue());
		if( count > 0 ) {
			return true;
		}
		return false;
	}

	/**
	 * 冻结金额累减， 可用余额累加
	 * @param id
	 * @param amount
	 * @return
	 */
	@Override
	@Lock("coffey-finance-Id-#{#id}")
	@Transactional(rollbackFor = Exception.class)
	public boolean addBalanceExtractFail(Long id, Long platformId, String amount) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		int count = bookMapper.addBalanceExtractFail(id, platformId, new BigDecimal(amount).longValue());
		if( count > 0 ) {
			return true;
		}
		return false;
	}

	@Value("${serviceBalance.order}")
	private String signKey;

	@Override
	public String sign(Map<String, String> map) {
		map.put("signkey", signKey);
		return PaySignUtil.sign(map);
	}

}
