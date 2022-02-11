package com.zevin.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zevin.rabbitmq.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 死信队列
 *
 * 消费者1
 */
public class Consumer02 {
    //普通交换机的名称
    public  static  final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机的名称
    public  static  final String DEAD_EXCHANGE = "dead_exchange";
    //普通队列名称
    public  static  final String NORMAL_QUEUE = "normal_queue";
    //死信队列名称
    public  static  final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args)throws Exception {
        Channel channel = RabbitMqUtils.channel();

        System.out.println("等待接受消息.....");

        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("Consumer02接收的消息是："+new String(message.getBody(),"UTF-8"));
        };
        CancelCallback cancelCallback = (var)->{

        };
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,cancelCallback);

    }
}
