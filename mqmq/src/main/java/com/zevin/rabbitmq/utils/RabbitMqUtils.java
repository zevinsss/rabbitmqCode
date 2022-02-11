package com.zevin.rabbitmq.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.*;


/**
 * 此类为连接工厂创建信道的工具类
 */

public class RabbitMqUtils {
    public static Channel channel()throws Exception{
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置工厂IP   目的：连接rabbitMQ队列
        factory.setHost("xxx.xx.xx.x");
        //用户名
        factory.setUsername("admin");
        //设置密码
        factory.setPassword("123");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        return channel;
    }
}
