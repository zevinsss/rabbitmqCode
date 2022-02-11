package com.zevin.rabbitmq.one;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

public class Producer {
    // 队列名称
    public  static final String QUEUE_NAME = "hello";

    //发消息
    public static void main(String[] args) throws Exception {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置工厂IP   目的：连接rabbitMQ队列
        factory.setHost("120.79.23.0");
        //用户名
        factory.setUsername("admin");

        //设置密码
        factory.setPassword("123");

        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();

        /**
         * 生成一个队列
         * （参数）1.队列名称
         * 2.队列里面的消息是否持久化（写入磁盘），可设为TRUE 和 FALSE
         * 3.该队列是否只供一个消费者进行消费 是否进行消息共享， TRUE可以多个消费者消费，FALSE只能一个消费者消费
         * 4.是否自动删除，最后一个消费者端开连接以后 ，该队列是否自动删除，TRUE自动删除，反之亦然
         * 5.其他参数，null即可
         *
         */
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-max-priority",10);
        channel.queueDeclare(QUEUE_NAME,true,false,false,arguments);

        for (int i = 1; i < 11; i++) {
            String message = "info"+i;
            if (i==5){
                AMQP.BasicProperties properties =new AMQP.BasicProperties()
                        .builder().priority(5).build();
                channel.basicPublish("",QUEUE_NAME,properties,message.getBytes());
            }else {
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            }
        }

        //发消息
        String message = "hello fucker";
        /**
         * 发送一个消费
         * 参数）1.发送到哪个交换机
         * 2.路由的key值是哪个 本次是队列的名称
         * 3.其他参数信息
         * 4.发送消息体
         */
       /* channel.basicPublish("",QUEUE_NAME,null,message.getBytes());*/
        System.out.println("消息发送完成");
    }
}
