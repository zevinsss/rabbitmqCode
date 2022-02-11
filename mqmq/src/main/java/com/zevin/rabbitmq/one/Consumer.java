package com.zevin.rabbitmq.one;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.AMQImpl;

public class Consumer {
    //队列名称 需要跟生产者一样
    public  static  final String QUEUE_NAME = "hello";
    //接收消息
    public static void main(String[] args) throws Exception{
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("120.79.23.0");
        factory.setUsername("admin");
        factory.setPassword("123");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /**
         * 消费者消费消息
         * (参数)
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 TRUE or FALSE
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         *
         */
        //声明 接受消息
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };
        //取消消息时回调
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("消息被中断");
        };
        System.out.println("C1等待接受消息");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
