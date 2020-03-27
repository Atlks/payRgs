package org.chwin.firefighting.apiserver.test;

import com.google.common.collect.Maps;
import com.platform.top.xiaoyu.run.service.api.finance.stream.FinanceOutSource;
import com.platform.top.xiaoyu.run.service.api.finance.stream.TypeMessage;
import com.platform.top.xiaoyu.run.service.api.finance.vo.req.recharge.RechargeInsertOnlineReq;
import com.platform.top.xiaoyu.run.service.finance.stream.FinanceSink;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

//@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = mqSend4jeff.class)
@EnableBinding({FinanceOutSource.class, FinanceSink.class})
public class mqSend4jeff {
    @Autowired
    FinanceOutSource FinanceOutSource1;


    @Autowired
    FinanceSink FinanceSink1;

//    @Autowired
//    SqlSession session;
//    @Autowired
//    SqlSessionFactory SqlSessionFactory1;


    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

//    @Autowired
//    SqlSessionDaoSupport SqlSessionDaoSupport1;


//@Autowired
//    FinanceSink FinanceSinkOut1;

    @Test
    public void contextLoads() {

        RechargeInsertOnlineReq req=new RechargeInsertOnlineReq();
        req.setAmount("200000000");
        Map m= Maps.newHashMap();
        m.put("充值类型","在线充值");
        m.put("user_id","216654713456709632");
        m.put("platformId","platformId");
        m.put("req",req);
   //     m.put("onlineResp",onlineResp);
        //发送消息给活动

        Map m3= Maps.newHashMap();
        m3.put("user_id","444");
        List<Map> li=    sqlSessionTemplate.selectList("recharge_count",m3);
       // List<Map> li=session.selectList("recharge_count",m3);
        System.out.println(li);

   //     FinanceOutSource1.financeToActivityOutput().send(MessageBuilder.withPayload(m).setHeader(TypeMessage.MESSAGETYPE, TypeMessage.TYPE_ACTIVITY_FINANCE_FIST).build());
    //    FinanceSink1.activityToFinanceInput().send(MessageBuilder.withPayload("tttt").setHeader(TypeMessage.MESSAGETYPE, TypeMessage. TYPE_ACTIVITY_FINANCE_FirstRecharge ).build() );
    }
 }
