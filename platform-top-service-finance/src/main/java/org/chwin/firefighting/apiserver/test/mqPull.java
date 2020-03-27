package org.chwin.firefighting.apiserver.test;

import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.Set;

public class mqPull {
  //  @SneakyThrows
//    public static void main(String[] args) {
//        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("please_rename_unique_group_name_5");
//        consumer.setNamesrvAddr("192.168.0.4:9876");
//        consumer.setInstanceName("consumer");
//        consumer.start();
//
//        Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("activity_to_finance_topic");
//        for (MessageQueue mq : mqs) {
//            System.out.printf("Consume from the queue: %s%n", mq);
//            SINGLE_MQ:
//            while (true) {
//                try {
//                    PullResult pullResult =
//                            consumer.pullBlockIfNotFound(mq, null, getMessageQueueOffset(mq), 32);
//                    System.out.printf("%s%n", pullResult);
//            //        putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
//                    switch (pullResult.getPullStatus()) {
//                        case FOUND:
//                            System.out.println(pullResult.getMsgFoundList().get(0).toString());
//                            break;
//                        case NO_NEW_MSG:
//                            break SINGLE_MQ;
//                        case NO_MATCHED_MSG:
//                        case OFFSET_ILLEGAL:
//                            break;
//                        default:
//                            break;
//                    }
//                } catch (Exception e) {
//                    System.out.println(e);
//                }
//            }
//        }
//        consumer.shutdown();
//    }
}
