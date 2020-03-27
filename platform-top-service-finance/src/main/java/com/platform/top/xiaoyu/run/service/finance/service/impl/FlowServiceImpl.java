package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.enums.*;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.stream.FinanceOutSource;
import com.platform.top.xiaoyu.run.service.api.finance.stream.TypeMessage;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BookVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.FlowVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountToolsVO;
import com.platform.top.xiaoyu.run.service.api.ws.fegin.IPushClient;
import com.platform.top.xiaoyu.run.service.finance.entity.Flow;
import com.platform.top.xiaoyu.run.service.finance.mapper.FlowMapper;
import com.platform.top.xiaoyu.run.service.finance.service.IBookApiService;
import com.platform.top.xiaoyu.run.service.finance.service.IBookService;
import com.platform.top.xiaoyu.run.service.finance.service.IFlowService;
import com.platform.top.xiaoyu.run.service.finance.service.IReceiptAccountToolsService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 我的账本流水 服务实现类
 *
 * @author coffey
 */
@Service
@Slf4j
public class FlowServiceImpl extends AbstractMybatisPlusService<FlowMapper, Flow, Long> implements IFlowService {

	@Autowired
	private FlowMapper flowMapper;
	@Autowired
	private IReceiptAccountToolsService toolsService;
	@Autowired
	private IBookService bookService;
    @Autowired
    private IBookApiService iBookApiService;
	@Autowired
	private IPushClient iPushClient;
	@Autowired
	private FinanceOutSource outSource;

	@Override
	public Page<FlowVO> findPage(Page<FlowVO> page, FlowVO flowVO) {
		return flowMapper.findPage(page, flowVO);
	}

	/**
	 * 查询条件封装
	 *
	 * @param platformId 平台ID
	 * @param ids 主键IDS
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	private QueryWrapper<Flow> getQuery(Long platformId, List<Long> ids, LocalDateTime beginDate, LocalDateTime endDate) {
		QueryWrapper<Flow> queryWrapper = new QueryWrapper<Flow>();
		queryWrapper.eq(Flow.P_PLATFORM_ID, platformId).between(Flow.P_CREATE_TIMESTAMP, beginDate, endDate);
		if(!CollectionUtils.isEmpty(ids)) {
			for (Long id : ids) {
				queryWrapper.ne(Flow.PK_ID, id);
			}
		}
		return queryWrapper;
	}

	@Override
	public List<FlowVO> findFlowListAll(Long platformId, List<Long> ids, LocalDateTime beginDate, LocalDateTime endDate) {
		return BeanCopyUtils.copyList(this.list(this.getQuery(platformId, ids, beginDate, endDate)), FlowVO.class);
	}

	@Override
	public Integer findFlowListCount(Long platformId, List<Long> ids, LocalDateTime beginDate, LocalDateTime endDate) {
		return this.count(this.getQuery(platformId, ids, beginDate, endDate));
	}

	@Override
	public FlowVO findLastFlowDetail(FlowVO flowVO) {
		if( null != flowVO && null != flowVO.getPlatformId() && flowVO.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return flowMapper.findLastFlowDetail(flowVO);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insertFlow(FlowVO flowVO) {
		if( null != flowVO && null != flowVO.getPlatformId() && flowVO.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}

		LocalDateTime localDateTime = LocalDateTime.now();
		flowVO.setCreateTimestamp(localDateTime);
		flowVO.setBusTimestamp(localDateTime);

		//状态
		BusStatusEnums statusEnums = BusStatusEnums.getType(flowVO.getStatuss());
		if( null != statusEnums) {
			flowVO.setStatussStr(statusEnums.getName());
		}

		//类型
		BusTypeEnums typeEnums = BusTypeEnums.getType(flowVO.getType());
		if( null != typeEnums) {
			flowVO.setTypeStr(typeEnums.getName());
		}

		//大类
		BusTypeAllEnums typeAllEnums = BusTypeAllEnums.getType(flowVO.getTypeAll());
		if( null != typeAllEnums) {
			flowVO.setTypeAllStr(typeAllEnums.getName());
		}

		if(null != flowVO.getTradingManner()) {
			//获取当前插入数据行， 解析当前的类型
			BusTradingMannerEnums trmangEnums = BusTradingMannerEnums.getType(flowVO.getTradingManner());
			//交易类型中文
			flowVO.setTradingMannerStr(trmangEnums != null ? trmangEnums.getName() : null);
			//查询交易类型的优惠， 对应查询收款账号设置的优惠政策
			ReceiptAccountToolsVO toolsVO = toolsService.findDetailType(flowVO.getTradingManner(), flowVO.getPlatformId());
			if( null != toolsVO) {
				//类型str
				flowVO.setTradingMannerStr(toolsVO.getName());
				//优惠备注
				flowVO.setTradingMannerOffStr(toolsVO.getOfferStr());
			}
		}
		//判断收入，支出，当前余额
		long amount = this.getMoney(BusTypeEnums.getType(flowVO.getType()), flowVO.getActualAmount());
		flowVO.setBalanceIn("0");
		flowVO.setBalanceOut("0");
		if(amount > 0 ) {
			flowVO.setBalanceIn(amount+"");
		} else {
			flowVO.setBalanceOut(amount+"");
		}
		//查询我的账本可用余额
		BookVO bookVO = bookService.findDetail(flowVO.getUserId(), flowVO.getPlatformId());
		flowVO.setBalance(bookVO.getBalance());

		//计算充值提现 总金额， 总次数
//		this.saveSumNumber(flowVO.getUserId(), flowVO.getPlatformId(), BusTypeEnums.getType(flowVO.getType()), BusStatusEnums.getType(flowVO.getStatuss()));

//      后续开启， 暂时不用
//		if (amount != 0) {
//			try {
//				//Websocket 金额变动，发送消息给手机客户端
//				TopicMessage message = new TopicMessage();
//				message.setTopic(BizMessage.BIZ_TYPE_MONEY_CHANGE);
//				message.setMessage(BizMessage.builder().type(BizMessage.BIZ_TYPE_MONEY_CHANGE).data(bookVO.getBalance()).build());
//				message.setPlatformId(flowVO.getPlatformId());
//
//				iPushClient.pushTopicMessage(message);
//			} catch (Exception ex) {
//				log.error("Websocket 发送失败"+ex.getMessage(), ex);
//			}
//		}

		flowVO.setId(idService.getNextId());
		if( this.save(BeanCopyUtils.copyBean(flowVO, Flow.class)) ) {
			//发送消息给ES
			outSource.financeToEsOutput().send(MessageBuilder.withPayload(flowVO).setHeader(TypeMessage.MESSAGETYPE, TypeMessage.TYPE_ES_FINANCE_FLOW).build());
			return true;
		}

		return false;

	}

	/**
	 * 计总次数 充值，人工充值， 在线提现，人工提现
	 * @param userId
	 * @param platformId
	 * @param typeEnums
	 */
//	private void saveSumNumber(Long userId, Long platformId, BusTypeEnums typeEnums, BusStatusEnums status) {
//		SumMoneyTypeEnums type = null;
//		if(typeEnums == BusTypeEnums.EXTRACT_OFFLINE || typeEnums == BusTypeEnums.EXTRACT_ONLINE) {
//			if(status == BusStatusEnums.EXTRACT_OK) {
//				type = SumMoneyTypeEnums.RECHARGE;
//			}
//		} else if(typeEnums == BusTypeEnums.RECHARGE_ONLINE || typeEnums == BusTypeEnums.RECHARGE_OFFLINE) {
//			if(status == BusStatusEnums.RECHARGE_OK) {
//				type = SumMoneyTypeEnums.EXTRACT;
//			}
//		}
//
//		if(null != type) {
//			iBookApiService.saveSumNumber(userId, platformId, type);
//		}
//
//	}



	/**
	 * 各种类型返回值， 系统平台转给用户为负，  用户转入系统平台为正数
	 * @param value 枚举类型
	 * @param amountStr 操作金额
	 * @return 返回 操作金额 正数、负数
	 */
	private long getMoney(BusTypeEnums value, String amountStr) {

		if( null == amountStr || new BigDecimal(amountStr).longValue() <= 0 ) { return 0; }

		long amount =  new BigDecimal(amountStr).longValue();
		//当前金额为空或为0， 返回0

		//平台 给用户转钱 返回正数、 否则返回负数
		switch (value) {
			case SYS_SIGNOUT: {
				amount = amount;
				break;
			}
			case RECHARGE_ONLINE: {
				amount = amount;
				break;
			}
			case ACTIVITY_REBATE: {
				amount = amount;
				break;
			}
			case ACTIVITY_PACK: {
				amount = amount;
				break;
			}
			case SYS_ORDEREND: {
				amount = amount;
				break;
			}
			case ACTIVITY_CODE: {
				amount = amount;
				break;
			}
			case ACTIVITY_LOTTERY: {
				amount = amount;
				break;
			}
			case ACTIVITY_OFFER: {
				amount = amount;
				break;
			}
			case RECHARGE_OFFLINE: {
				amount = amount;
				break;
			}
			case SAFE_OUT: {
				amount = amount;
				break;
			}
			case CHECKIN: {
				amount = amount;
				break;
			}
			case FRIST: {
				amount = amount;
				break;
			}
			case OFFER: {
				amount = amount;
				break;
			}
			case UNIVERSAL: {
				amount = amount;
				break;
			}
			case ARTIFICIAL_GIVING: {
				amount = amount;
				break;
			}
			case RECHARGE_MOSAIC_GOLD: {
				amount = amount;
				break;
			}
			case PLATFORM_MONEY: {
				amount = amount;
				break;
			}
			case ACTIVITY_CODE_ADD: {
				amount = -amount;
				break;
			}
			case EXTRACT_ONLINE: {
				amount = -amount;
				break;
			}
			case SYS_SIGNIN: {
				amount = -amount;
				break;
			}
			case SAFE_IN: {
				amount = -amount;
				break;
			}
			case EXTRACT_OFFLINE: {
				amount = -amount;
				break;
			}
			case EXTRACT_OK: {
				amount = -amount;
				break;
			}
			case EXTRACT_MONEY: {
				amount = -amount;
				break;
			}
			case RECHARGE_MONEY: {
				amount = -amount;
				break;
			}
			case EXTRACT_FAIL: {
				amount = -amount;
				break;
			}

		}
		return amount;
	}

}
