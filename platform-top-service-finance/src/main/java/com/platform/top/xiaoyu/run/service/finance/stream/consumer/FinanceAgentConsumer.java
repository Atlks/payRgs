package com.platform.top.xiaoyu.run.service.finance.stream.consumer;

import com.platform.top.xiaoyu.run.service.api.agent.stream.register.AgentParent;
import com.platform.top.xiaoyu.run.service.api.common.enums.CommonStatus;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeAllEnums;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeEnums;
import com.platform.top.xiaoyu.run.service.finance.constant.BusConstant;
import com.platform.top.xiaoyu.run.service.finance.service.IBalanceService;
import com.platform.top.xiaoyu.run.service.finance.service.IMsgBusService;
import com.platform.top.xiaoyu.run.service.finance.stream.FinanceSink;
import com.top.xiaoyu.rearend.tool.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class FinanceAgentConsumer {

	@Autowired
	private IBalanceService iBalanceService;
	@Autowired
	private IMsgBusService msgBusService;

	@StreamListener(value = FinanceSink.AGENT_TO_FINANCE_INPUT)
	public void readMessage(AgentParent.TransferCommission msg) {
		log.info("接收到消息==》 领取佣金 " + msg.toString());
		if ( null == msg ) {
			log.info("接收到消息==》 领取佣金 为 null ， 不处理。" + msg.toString());
			return;
		}
		if(null == msg.getId()) {
			log.info("佣金 消息ID 为空， 不处理 ");
			return;
		}
		if(StringUtil.isEmpty(msg.getTransferAmount())) {
			log.info("接收到消息==》 领取佣金 为 null ， 不处理。" + msg.toString());
			return;
		}
		CommonStatus status = CommonStatus.DISABLE;
		//转成金额
		long amount = new BigDecimal(msg.getTransferAmount()).longValue();
		if (amount <= 0) {
			log.info("接收到消息==》 领取佣金 为 0 ， 不处理。" + msg.toString());
			return;
		}
		if (!msgBusService.findMsg(msg.getId().toString(), msg.getPlatformId(), BusTypeEnums.ACTIVITY_REBATE) ) {
			log.info("接收到重复消息 ， 不处理。" + msg.toString());
			return;
		}

		String amountDB = msg.getTransferAmount();
		try {
			//领取佣金 新增我的账本可用余额 * 100 万
			String moeny = new BigDecimal(msg.getTransferAmount()).multiply(new BigDecimal(BusConstant.UTILS_DIVISOR)).toString();
			iBalanceService.execute(msg.getUserId(), msg.getPlatformId(), msg.getUserName(),
					moeny, BusTypeAllEnums.BALANCE_CALC, BusTypeEnums.ACTIVITY_REBATE, BusConstant.BALANCE_COMMISSION);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			msgBusService.insertMQ(msg.getUserId(), msg.getPlatformId(), msg.getId().toString(), amountDB, BusTypeEnums.ACTIVITY_REBATE, status, msg.toString());
		}

	}

}
