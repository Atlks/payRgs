package com.platform.top.xiaoyu.run.service.finance.stream.consumer;

import com.platform.top.xiaoyu.run.service.api.activity.stream.register.UserRegistMoney;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class FinanceActivityForFirstRechargeConsumer {


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

//	@StreamListener(value =FinanceSink.TOPIC_TEST)
//	public void receive(String msg) {
//	//	Processor.INPUT
//		System.out.println(msg);
//	}

}
