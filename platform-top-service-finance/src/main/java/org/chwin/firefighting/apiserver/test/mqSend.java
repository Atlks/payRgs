package org.chwin.firefighting.apiserver.test;

import com.google.common.collect.Maps;
import com.platform.top.xiaoyu.run.service.api.finance.stream.FinanceOutSource;
import com.platform.top.xiaoyu.run.service.api.finance.stream.TypeMessage;
import com.platform.top.xiaoyu.run.service.finance.stream.FinanceSink;
import com.platform.top.xiaoyu.run.service.finance.stream.FinanceSinkOut;
import com.platform.top.xiaoyu.run.service.finance.stream.MySource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Map;

//@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = mqSend.class)
@EnableBinding({FinanceOutSource.class, FinanceSink.class})
public class mqSend {
    @Autowired
    FinanceOutSource FinanceOutSource1;


    @Autowired
    FinanceSink FinanceSink1;


//@Autowired
//    FinanceSink FinanceSinkOut1;

    @Test
    public void contextLoads() {
       // FinanceSink1.activityToFinanceInput().send(MessageBuilder.withPayload("tttt").setHeader(TypeMessage.MESSAGETYPE, TypeMessage. TYPE_ACTIVITY_FINANCE_FirstRecharge ).build() );
    }
//    public static void main(String[] args) {
//
//     =new FinanceOutSource(){
//            @Override
//            public SubscribableChannel financeToUserOutput() {
//                return null;
//            }
//
//            @Override
//            public SubscribableChannel financeToEsOutput() {
//                return null;
//            }
//
//            @Override
//            public SubscribableChannel financeToActivityOutput() {
//                return new  SubscribableChannel(){
//                    @Override
//                    public boolean send(Message<?> message, long l) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean subscribe(MessageHandler messageHandler) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean unsubscribe(MessageHandler messageHandler) {
//                        return false;
//                    }
//                };
//            }
//        };
//        Map msg= Maps.newHashMap();
////        msg.put("platformId",platformId);
////        msg.put("充值记录",businessVO);  //里面包括了人员信息userid等
//        //发送消息给ES
//        FinanceOutSource1.financeToActivityOutput().send().build());
//
//    }
}
