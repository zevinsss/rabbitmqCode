package com.zevin.rabbitmq.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.zevin.rabbitmq.utils.RabbitMqUtils;

public class Producer {
    /**
     * 死信队列  之 生产者
     */
    //普通交换机的名称
    public  static  final String NORMAL_EXCHANGE = "normal_exchange";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.channel();
        //死信消息  设置TTL时间 单位是ms 10000ms=10s
      /*  AMQP.BasicProperties properties =
                new AMQP.BasicProperties()
                .builder().expiration("10000").build();*/

        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,message.getBytes());
        }
    }
}
