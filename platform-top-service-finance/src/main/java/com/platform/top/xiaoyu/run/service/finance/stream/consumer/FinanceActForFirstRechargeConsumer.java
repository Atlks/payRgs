package com.platform.top.xiaoyu.run.service.finance.stream.consumer;

import com.platform.top.xiaoyu.run.service.api.activity.stream.register.UserRegistMoney;
import com.platform.top.xiaoyu.run.service.api.common.enums.CommonStatus;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeAllEnums;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeEnums;
import com.platform.top.xiaoyu.run.service.api.finance.stream.TypeMessage;
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
public class FinanceActForFirstRechargeConsumer {


	/**
	 * NameServer 地址
	 */
	//@Value("${spring.cloud.dtream.rocketmq.binder.name-sever}")
	private String namesrvAddr;


	public final static String TOPIC_TEST = "TOPIC_TEST";

	private String TAG_TEST = "TAG_TEST";


	@Autowired
	private IBalanceService iBalanceService;
	@Autowired
	private IMsgBusService msgBusService;


	@StreamListener(value = FinanceSink.ACTIVITY_TO_FINANCE_INPUT)
	public void readMessage(@Header("messageType") String messageType, UserRegistMoney msg) {
		if( "9" != (messageType)) {
			return;
		}

		log.info("接收到消息==》 首充 " + msg.toString());
		if ( null == msg ) {
			log.info("接收到消息==》  为 null ， 不处理。" + msg.toString());
			return;
		}
		if(null == msg.getId()) {
			log.info("首充送彩金 消息ID 为空， 不处理 ");
			return;
		}
		if(StringUtil.isEmpty(msg.getAmount())) {
			log.info("接收到消息==》 首充送彩金 为 null ， 不处理。" + msg.toString());
			return;
		}
		CommonStatus status = CommonStatus.DISABLE;
		//转成金额
		long amount = new BigDecimal(msg.getAmount()).longValue();
		if (amount <= 0) {
			log.info("接收到消息==》 首充送彩金 为 0 ， 不处理。" + msg.toString());
			return;
		}
		if (!msgBusService.findMsg(msg.getId(), msg.getPlatformId(), BusTypeEnums.ACTIVITY_REGUSER) ) {
			log.info("接收到重复消息 ， 不处理。" + msg.toString());
			return;
		}

		String amountDB = msg.getAmount();
		try {
			//首充送彩金 新增我的账本可用余额 * 100 万
			iBalanceService.execute(msg.getUserId(), msg.getPlatformId(), msg.getUserName(),
					msg.getAmount(), BusTypeAllEnums.BALANCE_CALC, BusTypeEnums.ACTIVITY_REGUSER, BusConstant.ACTIVITY_REGUSER);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			msgBusService.insertMQ(msg.getUserId(), msg.getPlatformId(), msg.getId(), amountDB, BusTypeEnums.ACTIVITY_REGUSER, status, msg.toString());
		}

	}

}
