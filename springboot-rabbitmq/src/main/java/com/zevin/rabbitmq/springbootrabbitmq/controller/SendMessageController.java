package com.zevin.rabbitmq.springbootrabbitmq.controller;

import com.zevin.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 发送延迟消息
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        rabbitTemplate.convertAndSend("X","XA","消息来自TTL为10秒"+message);
        rabbitTemplate.convertAndSend("X","XB","消息来自TTL为40秒"+message);
        log.info("当前时间:{},发送一条信息给两个TTL队列:{}",new Date().toString(),message);
    }


    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message,@PathVariable String ttlTime){
        MessagePostProcessor messagePostProcessor = (msggg)->{
            msggg.getMessageProperties().setExpiration(ttlTime);
            return msggg;
        };
        rabbitTemplate.convertAndSend("X","XC",message,messagePostProcessor);
        log.info("当前时间:{},发送一条时长:{}信息给TTL队列QC:{}",new Date().toString(),ttlTime,message);
    }
    //开始发消息 基于插件的 消息及延迟的时间

    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message,@PathVariable Integer delayTime){
        MessagePostProcessor messagePostProcessor = (msggg)->{
            //发送消息的时候 延迟时长  单位ms    注意这里是setDelay 而不是setExpiration
            msggg.getMessageProperties().setDelay(delayTime);
            return msggg;
        };
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,DelayedQueueConfig.DELAYED_ROUTING_KEY,message,messagePostProcessor);
        log.info("当前时间:{},发送一条时长:{}信息给延迟队列delayed.queue:{}",new Date().toString(),delayTime,message);
    }

    //开始发消息 测试确认   发布确认
}
