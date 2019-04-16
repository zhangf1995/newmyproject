package com.myproject.rabbitmq.rabbitmqConfig;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.annotation.Configuration;

/**
 * @program: myproject
 * @description:
 * @author: zf
 * @create: 2019-04-16 14:23
 **/
public class MsgSendConfirmCallBack implements RabbitTemplate.ConfirmCallback{
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {

    }
}
