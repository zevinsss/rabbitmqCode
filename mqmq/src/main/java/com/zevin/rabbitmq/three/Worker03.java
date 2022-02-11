package com.zevin.rabbitmq.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zevin.rabbitmq.utils.RabbitMqUtils;
import com.zevin.rabbitmq.utils.SleepUtils;

/**
 * 消息在收订应答时是不丢失，放回队列中重新消费
 */
public class Worker03 {
    //队列名称
    public  static final  String TASK_QUEUE_NAME= "ack_queue";

    //接受消息
    public static void main(String[] args)throws Exception{

        Channel channel=   RabbitMqUtils.channel();
        System.out.println("C1等待接受消息处理时间较短");
      //采用手动应答
        boolean autoAck = false;


        DeliverCallback deliverCallback = (consumerTag,message)->{
            //沉睡1S
            SleepUtils.sleep(1);
            System.out.println("接受到的消息"+new String(message.getBody(),"UTF-8"));
            //手动应答
            /**
             * 1.消息的标记 tag
             * 2.是否批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //设置不公平分发
        int prefetchCount = 5;
        /**
         * prefetchCount 参数为0时是公平分发（轮训）；
         * 大于0时，此时为不公平分发，prefetchCount为允许堆积的消息数量
         */
        channel.basicQos(prefetchCount);
        CancelCallback cancelCallback = (message)->{

            System.out.println("消费者取消消费接口回调逻辑");
        };
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,cancelCallback);
    }
}
