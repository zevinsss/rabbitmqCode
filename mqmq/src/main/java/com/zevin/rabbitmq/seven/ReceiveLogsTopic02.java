package com.zevin.rabbitmq.seven;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zevin.rabbitmq.utils.RabbitMqUtils;

/**
 * 声明主题交换机 及相关队列
 * 
 * 消费者C2
 */

public class ReceiveLogsTopic02 {
    //交换机名称 
    public static final String EXCHANGE_NAME = "topic_logs";
    
    //接收消息
    public static void main(String[] args)throws Exception {
        Channel channel = RabbitMqUtils.channel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        //声明队列
        String queueName = "Q2";
        //声明队列
        /**
         * 参数
         * 1.队列名称
         * 2.是否持久化
         * 3.是否共享
         * 4.是否自动删除
         */
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind(queueName,EXCHANGE_NAME,"lazy.#");
        System.out.println("等待接受消息..");
        //接收消息
        DeliverCallback deliverCallback = (var1, var2)->{
            System.out.println("01Topic接收到的消息："+new String(var2.getBody(),"UTF-8"));
            System.out.println("接收列队："+queueName+"绑定键"+var2.getEnvelope().getRoutingKey());
        };
        CancelCallback cancelCallback = (var3)->{};
        channel.basicConsume(queueName,true,deliverCallback,cancelCallback);

    }
}
