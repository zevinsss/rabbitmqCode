package com.zevin.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zevin.rabbitmq.utils.RabbitMqUtils;

public class ReceiveLogsDirect02 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.channel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个队列
        channel.queueDeclare("disk",false,false,false,null);
        channel.queueBind("disk",EXCHANGE_NAME,"error");

        DeliverCallback deliverCallback = (var1,var2)->{
            System.out.println("01Direct控制台打印接收到的消息"+new String(var2.getBody(),"UTF-8"));
        };
        CancelCallback cancelCallback = (var3)->{};
        channel.basicConsume("console",true,deliverCallback,cancelCallback);

    }
}
