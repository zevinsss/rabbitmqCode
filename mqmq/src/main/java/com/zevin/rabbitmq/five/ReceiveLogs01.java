package com.zevin.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.zevin.rabbitmq.utils.RabbitMqUtils;

public class ReceiveLogs01 {

    /**
     * 消息接收
     */
    //交换机的名称
    public  static  final  String EXCHANGE_NAME =  "logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.channel();
        //声明一个交换机   参数（1.交换机名称  2.交换机类型  ）
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个队列 临时队列
        /**
         * 生成一个临时队列，队列名称是随机的
         * 当消费者与队列连接断开的时候，队列自动删除
         */
        String queue = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机与队列
         */
        channel.queueBind(queue,EXCHANGE_NAME,"132");
        System.out.println("等待接收消息，把接收到的消息打印在屏幕上...");
        DeliverCallback deliverCallback = (var1,var2)->{
            System.out.println("01控制台打印接收到的消息"+new String(var2.getBody(),"UTF-8"));
        };

        channel.basicConsume(queue,true,deliverCallback,var1->{});
    }
}
