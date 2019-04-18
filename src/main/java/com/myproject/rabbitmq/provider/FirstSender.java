package com.myproject.rabbitmq.provider;

import com.myproject.rabbitmq.exchangeConfig.ExchangeConfig;
import com.myproject.rabbitmq.rabbitmqConfig.RabbitmqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: myproject
 * @description: 第一个提供者
 * @author: zf
 * @create: 2019-04-16 15:12
 **/
@Component
public class FirstSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String message){
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE,RabbitmqConfig.QUEUE_ONE,message);
    }

    public void sendTwo(String message){
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE,RabbitmqConfig.QUEUE_TWO,message);
    }

    public void sendDelay(String message){
        rabbitTemplate.convertAndSend(RabbitmqConfig.DELAY_EXCHANGE,RabbitmqConfig.QUEUE_DELAY,message);
    }
}
