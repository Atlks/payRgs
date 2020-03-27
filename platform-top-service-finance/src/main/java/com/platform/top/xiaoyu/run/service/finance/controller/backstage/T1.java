package com.platform.top.xiaoyu.run.service.finance.controller.backstage;

import com.platform.top.xiaoyu.run.service.api.finance.stream.ActivityRechargeFirstMessage;
import com.platform.top.xiaoyu.run.service.api.finance.stream.EsRechargeFirstMessage;
import com.platform.top.xiaoyu.run.service.api.finance.stream.FinanceOutSource;
import com.platform.top.xiaoyu.run.service.api.finance.stream.TypeMessage;
import com.platform.top.xiaoyu.run.service.finance.stream.FinanceSink;
import com.top.xiaoyu.rearend.component.swagger.controller.BackstageController;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/test/")
@BackstageController
public class T1 {

    public static void main(String[] args) {
        System.out.println("---");
    }


    @Autowired
    FinanceOutSource FinanceOutSource1;


    @Autowired
    FinanceSink FinanceSink1;

    @GetMapping("/test")
    public String get(){

//        RechargeInsertOnlineReq req=new RechargeInsertOnlineReq();
//        req.setAmount("200000000");
//        req.setAccountId(11L);
//
//        UserRegistMoney msg=new UserRegistMoney();
//        msg.setUserId(216654713456709632L);
//        msg.setAmount("200000000");
//        msg.setPlatformId(195275261137129472L);
//
//
//
//
//        Map m= Maps.newHashMap();
//        m.put("充值类型","在线充值1111");
//        m.put("user_id","216654713456709632");
//        m.put("platformId","195275261137129472");
//        m.put("充值次数",1);
//
////        Map m= Maps.newHashMap();
////        m.put("user_id",businessVO.getUserId());
////        recharge_count_RecResult=session.selectList("recharge_count",m);
//
//
//
//        //发送消息给活动
//
//    Message msag=new Message(){
//        @Override
//        public Object getPayload() {
//            return m;
//        }
//
//        @Override
//        public MessageHeaders getHeaders() {
//            return null;
//        }
//    };



     //   msg2.setTime( 	LocalDateTime.now());
     //   msg2.setTypeMessage(TypeMessage.TYPE_FINANCE_TO_ES_FIRST_RECHARE);
        //  msg2.set
        EsRechargeFirstMessage EsRechargeFirstMessage1=new EsRechargeFirstMessage();
        EsRechargeFirstMessage1.setUserId(216654713456709632L);
        EsRechargeFirstMessage1.setPlatformId(195275261137129472L);
        EsRechargeFirstMessage1.setMoney( "1233000000");
        EsRechargeFirstMessage1.setData(LocalDateTime.now());
     FinanceOutSource1.financeToEsOutput().send(MessageBuilder.withPayload(EsRechargeFirstMessage1).setHeader(TypeMessage.MESSAGETYPE,TypeMessage.TYPE_FINANCE_TO_ES_FIRST_RECHARE).build());


        ActivityRechargeFirstMessage msg2=new ActivityRechargeFirstMessage();
        msg2.setUserId(216654713456709632L);
        msg2.setPlatformId(195275261137129472L);
        // msg2.setRechargeType("在线充值");
        msg2.setMoney("1233000000");
   FinanceOutSource1.financeToActivityOutput().send(MessageBuilder.withPayload(msg2).setHeader(TypeMessage.MESSAGETYPE,TypeMessage.TYPE__FINANCE_TO_ACT_FIRST_RECHARE).build());

        return "ok899020..";
    }
}
