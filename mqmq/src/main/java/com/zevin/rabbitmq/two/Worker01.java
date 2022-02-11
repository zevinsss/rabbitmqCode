package com.zevin.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.zevin.rabbitmq.utils.RabbitMqUtils;

public class Worker01 {

    //队列的名称
    public  static final String QUEUE_NAME = "hello";

    public static void main(String[] args)throws Exception {
      Channel channel= RabbitMqUtils.channel();

      DeliverCallback deliverCallback=(var1,var2)->{
          System.out.println("01接受到的消息为："+new String(var2.getBody()));
      };

        CancelCallback cancelCallback = (var1)->{
            System.out.println(var1+"01消息被取消");
        };

      //消息接收
        System.out.println("C2等待接受");
      channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
