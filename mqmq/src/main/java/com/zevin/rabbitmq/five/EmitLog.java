package com.zevin.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.zevin.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

public class EmitLog {
    /**
     * 发消息 交换机
     */

    //交换机的名称
    public  static  final  String EXCHANGE_NAME =  "logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.channel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"468",null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息："+message);
        }
    }
}
