package com.myproject.rabbitmq.customer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @program: myproject
 * @description: 第一个消费者
 * @author: zf
 * @create: 2019-04-16 16:27
 **/
@Component
@RabbitListener(queues = {"first_queue","second_queue"})
public class firstCustomer {
}
