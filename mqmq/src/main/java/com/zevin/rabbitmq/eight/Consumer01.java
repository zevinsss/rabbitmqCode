package com.zevin.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zevin.rabbitmq.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 死信队列
 *
 * 消费者1
 */
public class Consumer01 {
    //普通交换机的名称
    public  static  final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机的名称
    public  static  final String DEAD_EXCHANGE = "dead_exchange";
    //普通队列名称
    public  static  final String NORMAL_QUEUE = "normal_queue";
    //死信队列名称
    public  static  final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args)throws Exception {
        Channel channel = RabbitMqUtils.channel();

        //声明死信和普通交换机类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明普通队列
        /*设置最后一个参数*/
        Map<String,Object> arguments = new HashMap<>();
        //过期时间
       /* arguments.put("x-message-ttl",100000);*/
        //正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key","lisi");
        //设置正常队列的长度限制
        /*arguments.put("x-max-length",6);*/
        //声明正常队列
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);

        ////////////////////////////////////////////////////////////
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //绑定普通的交换机与普通的队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        //绑定死信的交换机与死信的队列
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
        System.out.println("等待接受消息.....");

        DeliverCallback deliverCallback = (consumerTag,message)->{
            String msg = new String(message.getBody(),"UTF-8");
            if (msg.equals("info5")){
                System.out.println("Consumer01接收的消息是："+msg+":此消息是被C1拒绝的");
                //拒绝消息并且不塞回normal队列， 让这条消息进入死信队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("Consumer01接收的消息是："+msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };
        CancelCallback cancelCallback = (var)->{

        };
        channel.basicConsume(NORMAL_QUEUE,false/*拒绝消息时需要，开启手动应答改为FALSE*/,deliverCallback,cancelCallback);

    }
}
