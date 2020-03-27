package org.chwin.firefighting.apiserver.mq;

import lombok.SneakyThrows;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class MqUtil {



    @SneakyThrows
    public static void main(String[] args) {

        DefaultMQProducer producer = new DefaultMQProducer("finance_to_activity_group");
        producer.setNamesrvAddr("192.168.0.4:9876");
        producer.start();
        MqUtil.sendMessage(producer,"activity_to_finance_topic", "tag11", "key" + String.valueOf(System.currentTimeMillis()), "msg...");
    }


    @SneakyThrows
    public static void sendMessage(DefaultMQProducer producer, String topic, String tag11, String key, String data) {
        System.out.println("MQ: 生产者发送消息 :" + data);
        Message message = null;

        //转换成字符数组
        byte[] messageBody = data.getBytes(RemotingHelper.DEFAULT_CHARSET);
        //创建消息对象 topic ,tag ,key ,body
        message = new Message(topic, tag11, key, messageBody);
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

    }

//    @PreDestroy
//    public void stop() {
//        if (producer != null) {
//            producer.shutdown();
//            System.out.println("MQ：关闭ProducerTest生产者");
//        }
//    }


}