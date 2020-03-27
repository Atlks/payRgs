package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.common.token.SecurityUtil;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTradingMannerEnums;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeAllEnums;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeEnums;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.exception.SafeExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BookVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.FlowVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.SafeVO;
import com.platform.top.xiaoyu.run.service.api.system.feign.IPlatformFeignClient;
import com.platform.top.xiaoyu.run.service.api.system.vo.PlatformInfoIdAndNameVO;
import com.platform.top.xiaoyu.run.service.api.system.vo.req.PlatformInfoQueryReq;
import com.platform.top.xiaoyu.run.service.finance.constant.BusConstant;
import com.platform.top.xiaoyu.run.service.finance.entity.*;
import com.platform.top.xiaoyu.run.service.finance.enums.InterestTypeEnums;
import com.platform.top.xiaoyu.run.service.finance.financeutils.StringIsChinaStrUtil;
import com.platform.top.xiaoyu.run.service.finance.mapper.SafeMapper;
import com.platform.top.xiaoyu.run.service.finance.service.*;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.api.R;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 保险箱 服务实现类
 *
 * @author xiaoyu
 */
@Service
@Slf4j
public class SafeServiceImpl extends AbstractMybatisPlusService<SafeMapper, Safe, Long> implements ISafeService {

	@Value("${pwd.mybook.pwdKey}")
	private String payPwdKey;

	@Autowired
	private SafeMapper safeMapper;
	@Autowired
	private IBookService bookService;
	@Autowired
	private ISafeRateService rateService;
	@Autowired
	private IPlatformFeignClient platformFeignClient;
	@Autowired
	private ISafeFlowService safeFlowService;
	@Autowired
	private IFlowService flowService;
	@Autowired
	private IInterestService interestService;

	@Override
	public Page<SafeVO> findPage(Page<SafeVO> page, SafeVO safeVO) {
		return safeMapper.findPage(page, safeVO);
	}

	@Override
	public SafeVO findDetailUserId(Long userId, Long platformId) {
		SafeVO vo = new SafeVO();
		vo.setUserId(userId);
		vo.setPlatformId(platformId);
		return this.findDetail(vo);
	}

	@Override
	public SafeVO findDetail(SafeVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return safeMapper.findDetail(vo);
	}

	@Override
	public BookVO findBalance(Long userId, Long platformId) {
		BookVO bookVO = bookService.findDetail(userId, platformId);
		if (null == bookVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		return bookVO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insert(SafeVO safeVO) {

		//查询当前用户保险箱是否存在
		SafeVO vo = this.findDetailUserId(safeVO.getUserId(), safeVO.getPlatformId());
		if( null != vo ) {
			return true;
		}

		Safe safe = BeanCopyUtils.copyBean(safeVO, Safe.class);

		LocalDateTime localDateTime = LocalDateTime.now();
		safe.setCreateTimestamp(localDateTime);
		safe.setPlatformId(safeVO.getPlatformId());
		safe.setPwd(MD5Util.string2MD5HexUpper(payPwdKey + safe.getPwd()));
		safe.setPlatformId(SecurityUtil.getAccessToken().getPlatformId());

		int count = safeMapper.insert(safe);

		if( count > 0 ) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateReset(Long userId, String pwd, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		//判断密码不能输入中文
		if ( StringIsChinaStrUtil.isChinese(pwd) ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PWD_CHINA);
		}


		SafeVO safeVO = new SafeVO();
		safeVO.setUserId(userId);
		safeVO.setPlatformId(platformId);
		safeVO = safeMapper.findDetail(safeVO);
		if( null == safeVO ) {
			throw new BizBusinessException(SafeExceptionType.SAFE_USER_FAIL);
		}

		UpdateWrapper<Safe> updateWrapper = new UpdateWrapper<Safe>();
		updateWrapper.eq(Safe.PK_ID, safeVO.getId()).eq(Safe.P_PLATFORM_ID, platformId);

		Safe entity = new Safe();
		entity.setId(safeVO.getId());
		entity.setPwd(MD5Util.string2MD5HexUpper(pwd));
		entity.setPlatformId(platformId);
		entity.setUpdateTimestamp(LocalDateTime.now());

		return this.update(entity, updateWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updatePwd(Long userId, String pwd, String newPwd, Long platformId) {
		SafeVO vo = findDetailUserId(userId, platformId);
		if( null == vo ) {
			throw new BizBusinessException(SafeExceptionType.SAFE_USER_FAIL);
		}
		if ( !vo.getPwd().equals(MD5Util.string2MD5HexUpper(payPwdKey + pwd)) ) {
			throw new BizBusinessException(SafeExceptionType.PWD_FAIL);
		}
		//判断密码不能输入中文
		if ( StringIsChinaStrUtil.isChinese(newPwd) ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PWD_CHINA);
		}

		Safe entity = new Safe();
		entity.setId(vo.getId());
		entity.setPwd(MD5Util.string2MD5HexUpper(payPwdKey + newPwd));
		entity.setUpdateTimestamp(LocalDateTime.now());

		UpdateWrapper<Safe> updateWrapper = new UpdateWrapper<Safe>();
		updateWrapper.eq(Safe.PK_ID, vo.getId()).eq(Safe.P_PLATFORM_ID, platformId);

		return this.update(entity, updateWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean sign (SafeVO safeVO) {
		SafeVO vo = findDetailUserId(safeVO.getUserId(), safeVO.getPlatformId());
		if( null == vo) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		if ( !vo.getPwd().equals(MD5Util.string2MD5HexUpper(payPwdKey + safeVO.getPwd())) ) {
			throw new BizBusinessException(SafeExceptionType.PWD_FAIL);
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void calcJob() {

		log.info("执行调度");
		//查询所有平台
		PlatformInfoQueryReq platformInfoQueryReq = new PlatformInfoQueryReq();
		R<List<PlatformInfoIdAndNameVO>> ret =  platformFeignClient.findPlatformList(platformInfoQueryReq);

		if(!ret.isSuccess()) {
			log.info("未查询到平台数据");
			//未查询到平台
			return;
		} else {
			//查询到所有平台
			List<PlatformInfoIdAndNameVO> list = ret.getData();
			for ( PlatformInfoIdAndNameVO platformInfoModelVO : list ) {
				this.execute(platformInfoModelVO.getId());
			}
		}

	}

	/**
	 * 手工执行计算
	 * @param userId 用户ID
	 * @param platformId 平台ID
	 * @param today 执行日期
	 */
	@Override
	public void calcUser(Long userId, Long platformId, LocalDate today) {
		List<SafeRate> safeRateList = this.getSafeRate(platformId);
		if ( CollectionUtils.isEmpty(safeRateList) ) {
			return;
		}
		//查询当前用户保险箱流水
		QueryWrapper<SafeFlow> safeFlowQueryWrapper = new QueryWrapper<SafeFlow>();
		safeFlowQueryWrapper.eq(SafeFlow.P_USER_ID, userId).eq(SafeFlow.P_PLATFORM_ID, platformId).lt(SafeFlow.P_CREATETIMERSTAMP, today)
			.orderByDesc(SafeFlow.P_CREATETIMERSTAMP).last("LIMIT 1");
		SafeFlow safeFlow = safeFlowService.getOne(safeFlowQueryWrapper);
		if( null != safeFlow && new BigDecimal(safeFlow.getBalanceSafe()).longValue() > 0 ) {
			//list转换map
			Map<Integer, BigDecimal> safeRateMap = safeRateList.stream().collect(Collectors.toMap(SafeRate :: getDayNum, SafeRate :: getRate));
			//执行计算
			this.cale( userId, platformId, safeFlow.getBalanceSafe(), safeRateMap, safeRateList.get(0).getDayNum(), today, safeRateList);
		}
	}

	/**
	 * 查询保险箱利率设置,  利率天数从 最高->1 排序
	 * @param platformId 平台ID
	 * @return
	 */
	private List<SafeRate> getSafeRate(Long platformId) {
		QueryWrapper<SafeRate> safeRateQueryWrapper = new QueryWrapper<SafeRate>();
		safeRateQueryWrapper.eq(SafeRate.P_PLATFORM_ID, platformId).orderByDesc(SafeRate.P_DAY_NUM);
		List<SafeRate> safeRateList = rateService.list(safeRateQueryWrapper);
		if ( CollectionUtils.isEmpty(safeRateList) ) {
			return null;
		}
		return safeRateList;
	}

	/**
	 * 定时器执行计算
	 * @param platformId
	 */
	private void execute(Long platformId) {
		List<SafeRate> safeRateList = this.getSafeRate(platformId);
		if ( CollectionUtils.isEmpty(safeRateList) ) {
			return;
		}

		//查询我的账本中  保险箱保管金额 > 0 的所有用户
		QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<Book>();
		bookQueryWrapper.eq(Book.P_PLATFORM_ID, platformId).gt(Book.P_BALANCE_SAFE, 0).orderByDesc(Book.P_UPDATETIMERSTAMP);
		List<Book> bookList = bookService.list(bookQueryWrapper);

		if( !CollectionUtils.isEmpty(bookList) ) {
			//把配置放入到map
			Map<Integer, BigDecimal> safeRateMap = safeRateList.stream().collect(Collectors.toMap(SafeRate :: getDayNum, SafeRate :: getRate));

			for (Book book : bookList) {
				if( new BigDecimal(book.getBalanceSafe()).longValue() > 0) {
					//查询用户下所有的保险流水
					this.cale( book.getUserId(), platformId, book.getBalanceSafe(), safeRateMap, safeRateList.get(0).getDayNum(), LocalDate.now(), safeRateList);
				}
			}
		}
	}


	/**
	 * 计算保险箱利息
	 * @param userId 用户ID
	 * @param platformId 平台ID
	 * @param amount 保险箱保管金额
	 * @param maxDay 最大利率
	 * @param today 计算日期
	 */
	private void cale(Long userId, Long platformId, String amount, Map<Integer, BigDecimal> safeRateMap, Integer maxDay, LocalDate today, List<SafeRate> rateList) {
		if( null == amount || new BigDecimal(amount).longValue() <= 0 || safeRateMap.isEmpty()) {
			return;
		}

		//查询当前用户， 是否已计算过利息
		QueryWrapper<Interest> interestQueryWrapper = new QueryWrapper<Interest>();
		interestQueryWrapper.eq(Interest.P_USER_ID, userId).eq(Interest.P_PLATFORM_ID, platformId)
			.eq(Interest.P_TYPE, InterestTypeEnums.SAFE.getVal()).eq(Interest.P_CALE_DATE, today);
		List<Interest> interestList = interestService.list(interestQueryWrapper);
		if(!CollectionUtils.isEmpty(interestList)) {
			throw new BizBusinessException(BaseExceptionType.SAFERATE);
		}

		//降序排序， 查询 当前用户 保险箱流水
		QueryWrapper<SafeFlow> safeFlowQueryWrapper = new QueryWrapper<SafeFlow>();
		safeFlowQueryWrapper.eq(SafeFlow.P_PLATFORM_ID, platformId).eq(SafeFlow.P_USER_ID, userId).gt(SafeFlow.P_BALANCE_SAFE, 0).lt(SafeFlow.P_CREATETIMERSTAMP, today)
			.orderByDesc(SafeFlow.P_CREATETIMERSTAMP).last("limit 2000");

		List<SafeFlow> safeFlowList = safeFlowService.list(safeFlowQueryWrapper);

		//总利息，当前用户所有的保险箱金额流水每笔所得利息累加
		long interestMoney = 0;

		//利率
		BigDecimal rate = new BigDecimal("0");
		if ( !CollectionUtils.isEmpty(safeFlowList) ) {

			//总保险箱余额
			long safeBalance = new BigDecimal(amount).longValue();
			for ( SafeFlow safeFlow : safeFlowList ) {
				//保险箱流水金额
				long safeFlowAmount = new BigDecimal(safeFlow.getAmount()).longValue();
				//计算利率
				rate = this.calcInterest(safeFlow.getCreateTimestamp(), safeRateMap, maxDay, today, rateList);
				//计算保险箱金额每次扣减
				safeBalance -= safeFlowAmount;
				if(safeBalance >= 0 ) {
					//百分数
					rate = rate.divide(new BigDecimal("100"));
					if(null != rate) {
						//计算当前所属 利率 * 本金
						interestMoney += rate.multiply(new BigDecimal(String.valueOf(safeFlowAmount))).longValue();
					}
				} else {
					//扣减为负数， 则用正数+负数，得出当前剩余实际余额
					long tempMoney = (safeFlowAmount + safeBalance);
					if (tempMoney > 0 ) {
						if(null != rate) {
							//计算当前所属 利率 * 本金
							interestMoney += rate.multiply(new BigDecimal(String.valueOf(tempMoney))).longValue();
						}
					}
					break;
				}
			}
			String interestMoneyStr = interestMoney +"";

			if(!StringUtil.isEmpty(interestMoneyStr) && Float.parseFloat(interestMoneyStr) > 0) {

				//当前用户， 可用余额累加
				if (!bookService.addBalanceUserRecharge(userId, platformId, interestMoneyStr)) {
					throw new BizBusinessException(BaseExceptionType.BALANCE);
				}

				/** 插入交易流水 */
				FlowVO flowVO = new FlowVO();

				flowVO.setAmount(interestMoneyStr);
				flowVO.setActualAmount(interestMoneyStr);
				flowVO.setUserId(userId);
				flowVO.setPlatformId(platformId);
				flowVO.setBusTimestamp(LocalDateTime.now());
				//系统-可用余额累加
				flowVO.setTypeAll(BusTypeAllEnums.BALANCE_CALC.getVal());
				//结算存入
				flowVO.setType(BusTypeEnums.SYS_ORDEREND.getVal());
				flowVO.setTradingManner(BusTradingMannerEnums.BUS_TRA_ADMIN_IN.getVal());
				//保险箱利息结算
				flowVO.setRemark(BusConstant.SAFE_CALE);
				flowVO.setDescription(BusConstant.SAFE_CALE);
				//插入交易流水
				if (!flowService.insertFlow(flowVO)) {
					//插入流水失败
					throw new BizBusinessException(BaseExceptionType.FLOW_FAIL);
				}
			}
		}

		//插入到计算表
		Interest interest = new Interest();
		interest.setUserId(userId);
		interest.setPlatformId(platformId);
		interest.setType(InterestTypeEnums.SAFE.getVal());
		interest.setCaleDate(today);
		interest.setMoney(interestMoney+"");
		interest.setParamCalc(rate+"");

		if ( !interestService.save(interest) ) {
			throw new BizBusinessException(BaseExceptionType.INTEREST_ADD);
		}
	}
	/**
	 * 从配置中获取利率
	 * @param dataTime 距离当前时间 N 天
	 * @param safeRateMap 利率配置
	 * @param maxDay 最大天数
	 * @param rateList 所有利率list
	 * @return 返回利率
	 */
	private BigDecimal calcInterest(LocalDateTime dataTime, Map<Integer, BigDecimal> safeRateMap, Integer maxDay, LocalDate today, List<SafeRate> rateList) {
		//计算数据日期，距离现在 多少天
		Long day = ChronoUnit.DAYS.between(dataTime.toLocalDate(), today);
		if ( null == day || day == 0) {
			return null;
		}
		//利率
		BigDecimal rate = safeRateMap.get(day);
		//取最大的利率
		if(day.longValue() >= maxDay.longValue()) {
			rate = safeRateMap.get(maxDay);
		} else {
			if ( null == rate ) {
				rateList.sort(Comparator.comparingInt(SafeRate::getDayNum).reversed());
				//计算相近的最大利率
				for ( SafeRate safeRate : rateList ) {
					Integer dayKey = safeRate.getDayNum();
					if( day >= dayKey ) {
						rate = safeRateMap.get(dayKey);
						break;
					}
				}
			}
		}
		if ( null == rate || rate.floatValue() <= 0 ) {
			return null;
		}

		// 利率
		return rate;
	}

}
