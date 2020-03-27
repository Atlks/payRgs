package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.es.feign.FinanceFlowFeignClient;
import com.platform.top.xiaoyu.run.service.api.es.vo.req.QueryUserFlowReq;
import com.platform.top.xiaoyu.run.service.api.es.vo.resp.QueryUserFlowResp;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeEnums;
import com.platform.top.xiaoyu.run.service.api.finance.enums.SumMoneyTypeEnums;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BookVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.mybook.BookApiReq;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.book.BookCountResp;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.book.BookFlowApiResp;
import com.platform.top.xiaoyu.run.service.finance.entity.Flow;
import com.platform.top.xiaoyu.run.service.finance.enums.BusTypeApiEnums;
import com.platform.top.xiaoyu.run.service.finance.service.*;
import com.top.xiaoyu.rearend.tool.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 绑卡 服务实现类
 *
 * @author coffey
 */
@Service
@Slf4j
public class BookApiServiceImpl implements IBookApiService {


	@Autowired
	private IBookService bookService;
	@Autowired
	private IFlowService flowService;
	@Autowired
	private ISumMoneyService moneyService;
	@Autowired
	private ISumMoneyFlowService moneyFlowService;
	@Autowired
	private FinanceFlowFeignClient financeFlowFeignClient;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveSumNumber(Long userId, Long platformId, SumMoneyTypeEnums enums) {

		//计总数据， 判断数据是否存在， 如果存在则调用一次更新所有类型数据
//		this.getMoney(userId, platformId, enums);
	}

	@Override
	public Page<BookFlowApiResp> findPage(BookApiReq req, Long userId, Long platformId) {

		//查询条件设置   ==> 默认 创建时间降序
		QueryWrapper<Flow> queryWrapper = new QueryWrapper<Flow>()
				.eq(Flow.P_PLATFORM_ID, platformId)
				.eq(Flow.P_USER_ID, userId);
		//获取查询条件
		queryWrapper = this.getQueryWrapper(queryWrapper, req).orderByDesc(Flow.P_CREATE_TIMESTAMP);

		//分页查询流水表数据
		IPage<Flow> retPage = flowService.page(new Page<Flow>(req.getPage(), req.getSize()), queryWrapper);

		//封装返回对象
		Page<BookFlowApiResp> ret = new Page<BookFlowApiResp>(retPage.getCurrent(), retPage.getSize());

		if ( !CollectionUtils.isEmpty(retPage.getRecords())) {

			//返回当前页的数据
			List<BookFlowApiResp> listResp = new ArrayList<BookFlowApiResp>();

			//装载
			for ( Flow flow : retPage.getRecords() ) {
				BookFlowApiResp resp = BookFlowApiResp.builder().balanceIn(flow.getBalanceIn()).balanceOut(flow.getBalanceOut()).balance(flow.getBalance()).build();
				resp.setStatus(flow.getType());
				resp.setStatusStr(flow.getTypeStr());
				resp.setUserId(userId);
				resp.setPlatformId(platformId);
				resp.setDateTime(flow.getBusTimestamp());
				listResp.add(resp);
			}
			ret.setRecords(listResp);
		}
		return ret;
	}

	/**
	 * 统计 计总  充值、提现、优惠、打码返水
	 * 获取同步 开始时间/获取计总表中最后一次计算的计总金额, 为空默认 0  ==》 1. 查询 计总表中数据 不存在 2. 查询 交易流水第一条记录
	 *
	 * @param userId
	 * @param platformId
	 * @return
	 */
	@Override
	public BookCountResp getData(Long userId, Long platformId) {
		//查询我的余额
		BookVO bookVO = bookService.findDetail(userId, platformId);

		BookCountResp ret = new BookCountResp();
		ret.setUserId(userId);
		ret.setPlatformId(platformId);
		//可用余额
		ret.setBalance(bookVO.getBalance());
		//保险柜金额
		ret.setBalanceSafe(bookVO.getBalanceSafe());

		try {
            //查询 RPC Es统计
            QueryUserFlowReq req = new QueryUserFlowReq();
            req.setUserId(userId);
            req.setPlatformId(platformId);
            R<QueryUserFlowResp> resp = financeFlowFeignClient.getFlowTotalByUser(req);
            if( null != resp) {
                QueryUserFlowResp userFlowResp = resp.getData();

                //优惠总额
                ret.setOffer(userFlowResp.getOfferSum());
                //充值总额、充值次数
                ret.setRecharge(userFlowResp.getRechargeSum());
                //提现总额、提现次数
                ret.setExtract(userFlowResp.getWithdrawSum());
                //打码/返水总额
                ret.setOrder(userFlowResp.getBetSum());
            }
        } catch (Exception ex) {
		    log.error("RPC调用 Es 统计接口错误"+ex.getMessage(), ex);
        }

		return ret;
	}

	/**
	 * 获取计总金额 ： 充值，提现，优惠，打码返水
	 * 获取同步 开始时间 优先顺序 1. 查询 计总表中数据 不存在 ==》 2. 查询 交易流水第一条记录
	 * 获取计总表中最后一次计算的计总金额, 为空默认 0
	 * @param userId 用户id
	 * @param platformId 平台id
	 * @param enums 枚举类型
	 * @return 返回计总金额
	 */
//	private String getMoney(Long userId, Long platformId, SumMoneyTypeEnums enums) {
//		//  上次同步剩余 统计金额
//		long amount = 0;
//		//上次同步剩余的总次数
//		int count = 0;
//		//开始时间
//		LocalDateTime beginDateTime = null;
//		//结束时间
//		LocalDateTime endDateTime = LocalDateTime.now();
//
//		//获取同步 开始时间 1. 查询 计总表中数据 不存在 2. 查询 交易流水第一条记录
//		//获取计总表中最后一次计算的计总金额, 为空默认 0
//		SumMoney sumMoney = this.getBeginDateTime(userId, platformId, enums);
//		if(null != sumMoney) {
//			beginDateTime = sumMoney.getLastDatetime();
//			if(!StringUtil.isEmpty(sumMoney.getSumAmount())) {
//				amount = new BigDecimal(sumMoney.getSumAmount()).longValue();
//			}
//			if(null != sumMoney.getSumNumber()) {
//				count = sumMoney.getSumNumber();
//			}
//		}
//
//		QueryWrapper<Flow> flowQueryWrapper = new QueryWrapper<Flow>();
//		flowQueryWrapper.eq(Flow.P_USER_ID, userId)
//			.eq(Flow.P_PLATFORM_ID, platformId)
//			.between(Flow.P_BUS_TIMESTAMP, beginDateTime, endDateTime);
//		switch (enums)  {
//			case RECHARGE: {
//				//人工充值、会员充值
//				List<Integer> recharge = new ArrayList<Integer>();
//				recharge.add(BusTypeEnums.RECHARGE_ONLINE.getValue());
//				recharge.add(BusTypeEnums.RECHARGE_OFFLINE.getValue());
//				flowQueryWrapper.in(Flow.P_TYPE, recharge).eq(Flow.P_STATUS, BusStatusEnums.RECHARGE_OK.getVal());
//				break;
//			}
//			case EXTRACT: {
//				//人工提现、会员提现
//				List<Integer> list = new ArrayList<Integer>();
//				list.add(BusTypeEnums.EXTRACT_OFFLINE.getValue());
//				list.add(BusTypeEnums.EXTRACT_ONLINE.getValue());
//				flowQueryWrapper.in(Flow.P_TYPE, list).eq(Flow.P_STATUS, BusStatusEnums.EXTRACT_OK.getVal());
//				break;
//			}
//			case OFFER: {
//				//优惠
//				flowQueryWrapper.eq(Flow.P_TYPE, BusTypeEnums.OFFER.getVal());
//				break;
//			}
//			case ACTIVITV_CODE: {
//				//打码、返水
//				flowQueryWrapper.eq(Flow.P_TYPE, BusTypeEnums.ACTIVITY_CODE.getValue());
//				break;
//			}
//		}
//
//		//不同类型， 计算总额度 && 加上累计值
//		List<Flow> listFlow = flowService.list(flowQueryWrapper);
//
//		if(!CollectionUtils.isEmpty(listFlow)) {
//			//计算计总金额
//			for ( Flow flowTemp : listFlow ) {
//				if(!StringUtil.isEmpty(flowTemp.getActualAmount())) {
//					amount += new BigDecimal(flowTemp.getActualAmount()).longValue();
//				}
//				count += 1;
//			}
//		}
//
//		if(amount == 0) {
//			return  amount+"";
//		}
//		if( null != sumMoney && !StringUtil.isEmpty(sumMoney.getSumAmount()) && amount == new BigDecimal(sumMoney.getSumAmount()).longValue() ) {
//			return amount+"";
//		}
//
//		//写入 计总表， 存在则更新
//		SumMoney entity = new SumMoney();
//
//		entity.setUserId(userId);
//		entity.setPlatformId(platformId);
//		entity.setType(enums.getVal());
//		entity.setSumAmount(amount+"");
//		entity.setSumNumber(count);
//		entity.setLastDatetime(endDateTime);
//
//		if( null != sumMoney ) {
//
//			QueryWrapper<SumMoney> queryWrapper = new QueryWrapper<SumMoney>();
//			queryWrapper.eq(SumMoney.PK_ID, sumMoney.getId()).eq(SumMoney.P_PLATFORM_ID, platformId);
//			moneyService.update(entity, queryWrapper);
//
//		} else {
//			moneyService.save(entity);
//		}
//
//		//插入同步记录
//		SumMoneyFlow moneyFlow = new SumMoneyFlow();
//		moneyFlow.setSumAmount(entity.getSumAmount());
//		moneyFlow.setUserId(userId);
//		moneyFlow.setPlatformId(platformId);
//		moneyFlow.setType(entity.getType());
//		moneyFlow.setLastDatetime(entity.getLastDatetime());
//		moneyFlow.setBeginDatetime(beginDateTime);
//		moneyFlow.setEndDatetime(endDateTime);
//
//		moneyFlowService.save(moneyFlow);
//
//		return amount+"";
//	}

	/**
	 * 获取同步 开始时间
	 * @param userId 用户id
	 * @param platformId 平台id
	 * @param enums 枚举类型
	 * @return 返回最后一次更新时间
	 */
//	private SumMoney getBeginDateTime(Long userId, Long platformId, SumMoneyTypeEnums enums) {
//		//查询最后一次同步时间
//		SumMoney sumMoney = this.getLastSumMoney(userId, platformId, enums);
//		if(null != sumMoney) {
//			return sumMoney;
//		}
//		//查询第一次交易记录
//		QueryWrapper<Flow> flowQueryWrapper = new QueryWrapper<Flow>();
//
//		switch (enums)  {
//			case RECHARGE: {
//				//人工充值、会员充值
//				List<Integer> list = new ArrayList<>();
//				list.add(BusTypeEnums.RECHARGE_OFFLINE.getValue());
//				list.add(BusTypeEnums.RECHARGE_ONLINE.getValue());
//				flowQueryWrapper.in(Flow.P_TYPE, list);
//				break;
//			}
//			case EXTRACT: {
//				//人工提现、会员提现
//				List<Integer> list = new ArrayList<>();
//				list.add(BusTypeEnums.EXTRACT_OFFLINE.getValue());
//				list.add(BusTypeEnums.EXTRACT_ONLINE.getValue());
//				flowQueryWrapper.in(Flow.P_TYPE, list).eq(Flow.P_STATUS, BusStatusEnums.EXTRACT_OK.getVal());
//				break;
//			}
//			case OFFER: {
//				//优惠
//				flowQueryWrapper.eq(Flow.P_TYPE, BusTypeEnums.OFFER.getVal());
//				break;
//			}
//			case ACTIVITV_CODE: {
//				//打码、返水
//				flowQueryWrapper.eq(Flow.P_TYPE, BusTypeEnums.ACTIVITY_CODE.getValue());
//				break;
//			}
//		}
//
//		flowQueryWrapper.eq(Flow.P_USER_ID, userId)
//			.eq(Flow.P_PLATFORM_ID, platformId)
//			.orderByAsc(Flow.P_BUS_TIMESTAMP)
//			.last("LIMIT 1");
//
//		Flow flow = flowService.getOne(flowQueryWrapper);
//
//		if(null == flow) {
//			return null;
//		}
//
//		sumMoney = new SumMoney();
//		sumMoney.setLastDatetime(flow.getCreateTimestamp());
//		//设置初始化值 0
//		sumMoney.setSumAmount("0");
//
//		return sumMoney;
//	}

	/**
	 * 获取计总表中最后一次计算的计总金额
	 * @param userId 用户id
	 * @param platformId 平台id
	 * @param type 计总类型  1 充值-人工充值、会员充值 2 提现-人工提现、会员提现 3 优惠 4 洗码/返水
	 * @return 返回数据行
	 */
//	private SumMoney getLastSumMoney(Long userId, Long platformId, SumMoneyTypeEnums type) {
//		QueryWrapper<SumMoney> sumMoneyQueryWrapper = new QueryWrapper<SumMoney>();
//		sumMoneyQueryWrapper.eq(SumMoney.P_TYPE, type.getVal())
//			.eq(SumMoney.P_USER_ID, userId)
//			.eq(SumMoney.P_PLATFORM_ID, platformId);
//
//		return moneyService.getOne(sumMoneyQueryWrapper);
//	}

	/**
	 * 获取查询条件，  时间段
	 * @param queryWrapper
	 * @param req
	 * @return
	 */
	private QueryWrapper<Flow> getQueryWrapper(QueryWrapper<Flow> queryWrapper, BookApiReq req) {
		//根据前端参数 1 今天， -1 昨天， 30 一个月内， 10000 所有时间
		if( req.getDay()!= null) {
			if (req.getDay().intValue() == 1) {
				queryWrapper.between(Flow.P_CREATE_TIMESTAMP, LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
			} else if (req.getDay().intValue() == -1) {
				queryWrapper.between(Flow.P_CREATE_TIMESTAMP, LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN), LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX));
			} else if (req.getDay().intValue() == 30) {
				queryWrapper.between(Flow.P_CREATE_TIMESTAMP, LocalDateTime.of(LocalDate.now().minusDays(30), LocalTime.MIN), LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX));
			}
		}

		//根据不同状态码，封装不同查询条件
		queryWrapper = this.getApiStatus(queryWrapper, BusTypeApiEnums.getType(req.getStatus()));

		return queryWrapper;
	}

	/**
	 * 前台 状态 == 后台数据库中的类型
	 * @param queryWrapper
	 * @param value
	 * @return
	 */
	private QueryWrapper<Flow> getApiStatus(QueryWrapper<Flow> queryWrapper, BusTypeApiEnums value) {
		if( null == value ) {
			return queryWrapper;
		}
		switch (value) {
			case All: {
				break;
			}
			case SAFE_ALL: {
				List<Integer> listValue = new ArrayList<Integer>();
				listValue.add(BusTypeEnums.SAFE_IN.getValue());
				listValue.add(BusTypeEnums.SAFE_OUT.getValue());
				queryWrapper.in(Flow.P_TYPE, listValue);
				break;
			}
			default: {
				queryWrapper.eq(Flow.P_TYPE, value.getVal());
				break;
			}

		}
		return queryWrapper;
	}

}
