package com.zevin.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.zevin.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * 测试目的：
 * 消息在手动应答时是不丢失的
 */

public class Task2 {

    //队列名称
    public static  final  String TASK_QUEUE = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel =  RabbitMqUtils.channel();
        //声明队列
        boolean durable = true;//持久化
        channel.queueDeclare(TASK_QUEUE,durable,false,false,null);
       //从控制台中输入信息
        Scanner scanner =new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",TASK_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN/*消息持久化*/,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息:"+message);
        }
    }
}
