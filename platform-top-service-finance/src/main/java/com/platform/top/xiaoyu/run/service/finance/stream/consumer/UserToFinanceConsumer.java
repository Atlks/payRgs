package com.platform.top.xiaoyu.run.service.finance.stream.consumer;

import com.platform.top.xiaoyu.run.service.api.common.enums.CommonStatus;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeAllEnums;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeEnums;
import com.platform.top.xiaoyu.run.service.api.user.stream.UserGetCashAsGivingMessage;
import com.platform.top.xiaoyu.run.service.api.user.stream.UserMessage;
import com.platform.top.xiaoyu.run.service.finance.constant.BusConstant;
import com.platform.top.xiaoyu.run.service.finance.service.IBalanceService;
import com.platform.top.xiaoyu.run.service.finance.service.IBookService;
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
public class UserToFinanceConsumer {

	@Autowired
	private IBalanceService iBalanceService;
	@Autowired
	private IMsgBusService msgBusService;
    @Autowired
    private IBookService iBookService;

	@StreamListener(value = FinanceSink.USER_TO_FINANCE_INPUT)
	public void readUserMessage(@Header("messageType") String messageType, UserGetCashAsGivingMessage msg) {
        log.info("接收到用户系统消息 == 》" + messageType);
	    if(StringUtil.isEmpty(messageType)) {
	        return;
        }
        if (null == msg) {
            log.info("接收到消息内容==》 null ， 不处理  " + msg.toString());
            return;
        }

        if(UserMessage.TYPE_REGIST_MSG == Integer.parseInt(messageType)) {
            log.info("当前消息 ！= 用户注册 " + messageType);
            //新注册用户， 新增账本
            try {
                iBookService.inserBook(msg.getUserId(), msg.getPlatformId(), msg.getUserName());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return;
        }

		if(UserMessage.TYPE_GET_CASHASGIVING != Integer.parseInt(messageType)) {
            log.info("当前消息 ！= 会员晋级礼金 ， 不处理  " + messageType);
			return;
		}

        log.info("接收到消息==》 会员晋级礼金 " + msg.toString());
        if (null == msg.getLotteryCollectionRecordId()) {
            log.info(" 会员晋级礼金 消息ID 为空， 不处理 ");
            return;
        }
        if (StringUtil.isEmpty(msg.getMoney())) {
            log.info("接收到消息==》 会员晋级礼金 金额为 null ， 不处理  " + msg.toString());
            return;
        }
        //转成金额
        long amount = new BigDecimal(msg.getMoney()).longValue();
        if (amount <= 0) {
            log.info("接收到消息==》 会员晋级礼金 金额为 0 ， 不处理  " + msg.toString());
            return;
        }
        //接收消息类型，区分 每个 类型
        BusTypeEnums enums = BusTypeEnums.getType(msg.getBusTypeEnums().getVal());
        if (enums != BusTypeEnums.VIP_UP && enums != BusTypeEnums.VIP_WEEK && enums != BusTypeEnums.VIP_MONTH) {
            log.info("接收到消息==》 会员 " + enums.getName() + " 类型不正确 ， 不处理  " + msg.toString());
            return;
        }

        if (!msgBusService.findMsg(msg.getLotteryCollectionRecordId().toString(), msg.getPlatformId(), enums)) {
            log.info("接收到重复消息 ， 不处理。" + msg.toString());
            return;
        }

        CommonStatus status = CommonStatus.DISABLE;
        String amountDB = msg.getMoney();
        try {
            //领取会员晋级礼金 累加可用余额
            iBalanceService.execute(msg.getUserId(), msg.getPlatformId(), msg.getUserName(), msg.getMoney(), BusTypeAllEnums.BALANCE_CALC, enums, enums.getName() + BusConstant.VIP);
            status = CommonStatus.ENABLE;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            msgBusService.insertMQ(msg.getUserId(), msg.getPlatformId(), msg.getLotteryCollectionRecordId().toString(), amountDB, enums, status, msg.toString());
        }
	}
}
