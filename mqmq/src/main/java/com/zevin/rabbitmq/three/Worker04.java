package com.zevin.rabbitmq.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zevin.rabbitmq.utils.RabbitMqUtils;
import com.zevin.rabbitmq.utils.SleepUtils;

/**
 * 消息在收订应答时是不丢失，放回队列中重新消费
 */
public class Worker04 {
    //队列名称
    public  static final  String TASK_QUEUE_NAME= "ack_queue";

    //接受消息
    public static void main(String[] args)throws Exception{

        Channel channel=   RabbitMqUtils.channel();
        System.out.println("C2等待接受消息处理时间较长");
        //采用手动应答
        boolean autoAck = false;


        DeliverCallback deliverCallback = (consumerTag,message)->{
            //沉睡1S
            SleepUtils.sleep(30);
            System.out.println("接受到的消息"+new String(message.getBody(),"UTF-8"));
            //手动应答
            /**
             * 1.消息的标记 tag
             * 2.是否批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);
        CancelCallback cancelCallback = (message)->{

            System.out.println("消费者取消消费接口回调逻辑");
        };
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,cancelCallback);
    }
}
