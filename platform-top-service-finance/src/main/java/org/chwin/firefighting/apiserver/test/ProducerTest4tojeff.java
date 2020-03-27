package org.chwin.firefighting.apiserver.test;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.platform.top.xiaoyu.run.service.api.finance.stream.FinanceOutSource;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageType;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.Map;

public class ProducerTest4tojeff {
    //nameserver地址
    private String namesrvAddr = "192.168.0.4:9876";

    private final DefaultMQProducer producer = new DefaultMQProducer("finance_to_activity_group");

    private String TOPIC_TEST = "finance_to_activity_topic";


    private String TAG_TEST = "";    //TAG_TEST


    FinanceOutSource financeOutSource;
    /**
     * 初始化
     */
    public void start() {
     try {
            System.out.println("MQ：启动ProducerTest生产者");
            producer.setNamesrvAddr(namesrvAddr);
            producer.start();
//            //发送消息
//            for(int i=0;i<100;i++) {
//                sendMessage("hello mq" + i);

       } catch (MQClientException e) {
           System.out.println("MQ：启动ProducerTest生产者失败：" + e.getResponseCode() + e.getErrorMessage());
           throw new RuntimeException(e.getMessage(), e);
      }
    }

    public void sendMessage(String data) {
        System.out.println("MQ: 生产者发送消息 :{}" +  data);
        Message message = null;
        try {
            //转换成字符数组
            byte[] messageBody = data.getBytes(RemotingHelper.DEFAULT_CHARSET);
            //创建消息对象 topic ,tag ,key ,body
            message = new Message(TOPIC_TEST, TAG_TEST, String.valueOf(System.currentTimeMillis()), messageBody);
    //        financeOutSource.financeToActivityOutput().send(MessageBuilder.withPayload(message).setHeader("messageType",Message));

            //同步发送消息
            SendResult sendResult = producer.send(message);
            //异步发送消息
            /*producer.send(message, new SendCallback() {
                public void onSuccess(SendResult sendResult) {
                    System.out.println("MQ: CouponProducer生产者发送消息" + sendResult);
                }

                public void onException(Throwable throwable) {
                    System.out.println(throwable.getMessage() +  throwable);
                }
            });*/
            //单向发送 只发送消息，不等待服务器响应，只发送请求不等待应答。
            //producer.sendOneway(message);
        } catch (Exception e) {
            if (message != null) {
                System.out.println("producerGroup:ProducerTest,Message:{}" + JSON.toJSON(message));
            }
            System.out.println("MQ: CouponProducer error :" + e);
        }
    }

    @PreDestroy
    public void stop() {
        if (producer != null) {
            producer.shutdown();
            System.out.println("MQ：关闭ProducerTest生产者");
        }
    }
@SneakyThrows
    public static void main(String[] args) {
        ProducerTest4tojeff producerTest = new ProducerTest4tojeff();
        producerTest.start();


        Map msg= Maps.newHashMap();
        msg.put("platformId","212291514477350912");
        BusinessVO businessVO=new BusinessVO();
        businessVO.setUserId(212291514477350912L);
         msg.put("充值记录",businessVO);  //里面包括了人员信息userid等
        msg.put("messageType",8);
    //String data = JSON.toJSONString(msg);
        String data = FileUtils.readFileToString(new File("d:\\msg.txt"),"gbk");

        System.out.println(data);
      producerTest.sendMessage(data);
    }
}