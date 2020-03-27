package com.platform.top.xiaoyu.run.service.finance.stream.consumer;

import com.platform.top.xiaoyu.run.api.operate.stream.OperateMessage;
import com.platform.top.xiaoyu.run.api.operate.stream.UserWashMsg;
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
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class FinanceOperateConsumer {

	@Autowired
	private IBalanceService iBalanceService;
	@Autowired
	private IMsgBusService msgBusService;

	@StreamListener(value = FinanceSink.OPERATE_TO_FINANCE_INPUT)
	public void readUserMessage(@Header("messageType") String messageType, UserWashMsg msg) {
		if(OperateMessage.TYPE_USER_WASH_MSG != Integer.parseInt(messageType)) {
			return;
		}

		log.info("接收到消息==》 领取洗码 "+msg.toString());
		if ( null == msg ) {
			log.info("接收到消息==》 领取洗码 为 null ， 不处理  "+msg.toString());
			return;
		}

		if(null == msg.getWashId()) {
			log.info(" 洗码 消息ID 为空， 不处理 ");
			return;
		}

		if(StringUtil.isEmpty(msg.getWashTheAmountOfCode())) {
			log.info("接收到消息==》 领取洗码 金额为 null ， 不处理  "+msg.toString());
			return;
		}

		//转成金额
		long amount = new BigDecimal(msg.getWashTheAmountOfCode()).longValue();
		if (amount <= 0) {
			log.info("接收到消息==》 领取洗码 金额为 0 ， 不处理  "+msg.toString());
			return;
		}

		if (!msgBusService.findMsg(msg.getWashId().toString(), msg.getPlatformId(), BusTypeEnums.ACTIVITY_CODE) ) {
			log.info("接收到重复消息 ， 不处理。" + msg.toString());
			return;
		}

		CommonStatus status = CommonStatus.DISABLE;
		String amountDB = msg.getWashTheAmountOfCode();
		try {
			//领取洗码 累加可用余额
			iBalanceService.execute(msg.getUserId(), msg.getPlatformId(), msg.getUserName(),
					msg.getWashTheAmountOfCode(), BusTypeAllEnums.BALANCE_CALC, BusTypeEnums.ACTIVITY_CODE, BusConstant.OPERATE);
			status = CommonStatus.ENABLE;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			msgBusService.insertMQ(msg.getUserId(), msg.getPlatformId(), msg.getWashId().toString(), amountDB, BusTypeEnums.ACTIVITY_CODE, status, msg.toString());
		}

	}

}
