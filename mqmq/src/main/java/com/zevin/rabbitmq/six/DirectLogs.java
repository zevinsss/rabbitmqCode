package com.zevin.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.zevin.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

public class DirectLogs {
    //交换机的名称
    public  static  final  String EXCHANGE_NAME =  "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.channel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            /**
             * 生产者发布消息
             * 参数：
             * 1.队列名称
             * 2.routingKey
             * 3.
             * 4.消息内容（需要以字节形式传输）
             */
            channel.basicPublish(EXCHANGE_NAME,"info",null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息："+message);
        }
    }
}
