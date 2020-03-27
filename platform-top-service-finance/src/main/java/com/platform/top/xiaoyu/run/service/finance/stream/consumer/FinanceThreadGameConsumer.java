package com.platform.top.xiaoyu.run.service.finance.stream.consumer;

import com.platform.top.xiaoyu.run.service.api.common.enums.CommonStatus;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeEnums;
import com.platform.top.xiaoyu.run.service.api.threadgame.stream.LoginOutParent;
import com.platform.top.xiaoyu.run.service.finance.constant.BusConstant;
import com.platform.top.xiaoyu.run.service.finance.service.IBookService;
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
public class FinanceThreadGameConsumer {

	@Autowired
	private IBookService bookService;
	@Autowired
	private IMsgBusService msgBusService;

	@StreamListener(value = FinanceSink.THREADGAME_TO_FINANCE_INPUT)
	public void readMessage(LoginOutParent.LoginOutMsg msg) {
		log.info("接收到消息==》 登出游戏，可用余额累加 消息为" + msg.toString());
		if ( null == msg ) {
			log.info("接收到消息==》 登出游戏 为 null ， 不执行可用余额累加" + msg.toString());
			return;
		}

		if(StringUtil.isEmpty(msg.getAmount())) {
			log.info("接收到消息==》 登出游戏，可用余额 为 null ， 不执行可用余额累加" + msg.toString());
			return;
		}

		long amount = new BigDecimal(msg.getAmount()).longValue();
		if( amount <= 0 ) {
			log.info("接收到消息==》 登出游戏，可用余额 为 0 ， 不执行可用余额累加" + msg.toString());
			return;
		}
		if (!msgBusService.findMsg(msg.getId().toString(), msg.getPlatformId(), BusTypeEnums.SYS_SIGNOUT) ) {
			log.info("接收到重复消息 ， 不处理。" + msg.toString());
			return;
		}

		CommonStatus status = CommonStatus.DISABLE;

		//参数可用余额累加 * 100 万
		String amountDB = new BigDecimal(msg.getAmount()).multiply(new BigDecimal(BusConstant.UTILS_DIVISOR)).toString();

		try {
			if(null == msg.getId()) {
				log.info("登出游戏 消息ID 为空， 不处理 ");
				return;
			}
			//登出游戏， 可用余额累加
			try {
				bookService.signOut(msg.getUserId(), msg.getPlatformId(), amountDB, msg.getGameName(), msg.getGameType());
			}catch(Exception e)
			{
		//		bookService.signOut(msg.getUserId(), msg.getPlatformId(), amountDB, msg.getGameName(), msg.getGameType());
			}

			status = CommonStatus.ENABLE;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			msgBusService.insertMQ(msg.getUserId(), msg.getPlatformId(), msg.getId().toString(), amountDB, BusTypeEnums.SYS_SIGNOUT, status, msg.toString());
		}
	}

}
