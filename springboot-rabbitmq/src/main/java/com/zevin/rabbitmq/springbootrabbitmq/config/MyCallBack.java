package com.zevin.rabbitmq.springbootrabbitmq.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {
    /**
     * 交换机确认回调方法
     * 情况1 ：发消息 交换机接收到了    回调
     *  此时参数所表达的意义
     *  1.1 correlationData 保存回调消息的ID及相关信息
     *  1.2 交换机收到的消息 ack=TRUE
     *  1.3 cause null  回调失败的原因，此时回调成功为null
     * 情况2 ：发消息 交换机接接收失败了 回调
     *  2.1 correlationData 保存回调消息的ID及相关信息
     *  2.2 交换机收到消息 ack=FALSE
     *  2.3 cause 失败的原因
     */
    @Autowired
    private RabbitTemplate rabbitTemplate ;

    @PostConstruct
    public void  init(){
        //注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData.getId()!=null?correlationData.getId():"";
        if (ack){
            log.info("交换机已经收到了ID为：{}的消息",id);
        }else {
            log.info("交换机还未收到ID为：{}的消息，原因是：{}",id,cause);
        }
    }

    //可以在消息传递过程中不可达目的地时将消息返回给生产者
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("消息：{}，被交换机：{}退回，退回原因：{}，路由key是：{}",returnedMessage.getMessage().getBody(),returnedMessage.getExchange(),returnedMessage.getReplyText(),returnedMessage.getRoutingKey());
    }
}
