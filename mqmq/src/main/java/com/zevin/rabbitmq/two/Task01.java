package com.zevin.rabbitmq.two;

import com.rabbitmq.client.Channel;
import com.zevin.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * 生产者代码
 */
public class Task01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args)throws Exception {
        Channel channel= RabbitMqUtils.channel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //从控制台中接受信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送消息完成："+message);
        }
    }
}
