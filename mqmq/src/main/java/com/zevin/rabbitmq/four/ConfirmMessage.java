package com.zevin.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.zevin.rabbitmq.utils.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认模式
 * 1.单个确认模式
 * 2.批量确认模式
 * 3.异步批量确认模式
 */
public class ConfirmMessage {
    //批量发消息的个数
    public  static  final  int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception{
        //* 1.单个确认模式
        /*publishMessageIndividually();*/
        // * 2.批量确认模式
        /*ConfirmMessage.publishMessageBatch();*/
        // * 3.异步批量确认模式
        publishMessageAsync();

    }

    //单个确认
   public static  void  publishMessageIndividually()throws Exception{
       Channel channel = RabbitMqUtils.channel();
       //队列的声明
       String ququeName = UUID.randomUUID().toString();
       channel.queueDeclare(ququeName,true,false,false,null);
       //开启发布确认
       channel.confirmSelect();
       //开始时间
       long begin = System.currentTimeMillis();

       //批量发消息
       for (int i = 0; i < MESSAGE_COUNT; i++) {
           String messag = i + "";
           channel.basicPublish("",ququeName,null,messag.getBytes());
           //单个消息就马上进行发布确认
          boolean flag  =  channel.waitForConfirms();
          /*if (flag){
              System.out.println("消息发送成功");
          }*/
       }
       //结束时间
       long end = System.currentTimeMillis();
       System.out.println("发布"+MESSAGE_COUNT+"个单独确认消息，耗时"+(end-begin));
   }

    //批量发布确认
    public static  void  publishMessageBatch()throws Exception{
        Channel channel = RabbitMqUtils.channel();
        //队列的声明
        String ququeName = UUID.randomUUID().toString();
        channel.queueDeclare(ququeName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量确认消息大小
        int batchSize = 100;

        //批量发消息  （批量发布确认）！！！！！！

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("",ququeName,true,null,message.getBytes());

            if (i+1%batchSize == 0){
                channel.waitForConfirms();
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个批量确认消息，耗时"+(end-begin));
    }

    //异步发布 确认
    public  static  void  publishMessageAsync() throws Exception{

        Channel channel = RabbitMqUtils.channel();
        //队列的声明
        String ququeName = UUID.randomUUID().toString();
        channel.queueDeclare(ququeName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表  使用于高并发的情况下
         * 功能：1.轻松的将序号于消息关联
         * 2.轻松的批量删除条目 只要给到序号
         * 3.支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //消息确认成功回调函数
        /**
         * ConfirmCallback 的（参数）
         * 1.var1 消息的标记
         * 2.var3 是否为批量确认
         */
        ConfirmCallback ackcallback = (var1,var3)->{
            if (var3){
                //（2）删除掉已经确认的消息  剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(var1);
                confirmed.clear();
            }else {
                outstandingConfirms.remove(var1);
            }
            System.out.println("确认的消息"+var1);
        };
        //消息确认失败回调函数
        ConfirmCallback nackcallback = (var1,var3)->{
            //（3） 打印一下未确认的消息有哪些
            String message = outstandingConfirms.get(var1);
            System.out.println("未确认的消息是："+message+":::::未确认的消息tag:"+var1);
        };
        /**
         * 1.监听哪些消息成功了
         * 2.监听哪些消息失败了
         */
        //准备消息的监听器 监听哪些成功了 哪些消息失败了
        channel.addConfirmListener(ackcallback,nackcallback);

        //开始时间
        long begin = System.currentTimeMillis();
        //批量发送
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + i ;
            channel.basicPublish("",ququeName,null,message.getBytes());
            //（1）记录下所有要发送的消息  消息的总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);

        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个异步发布确认消息，耗时"+(end-begin));
    }
}
