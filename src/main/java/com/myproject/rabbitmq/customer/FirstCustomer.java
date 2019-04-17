package com.myproject.rabbitmq.customer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @program: myproject
 * @description: 第一个消费者
 * @author: zf
 * @create: 2019-04-16 16:27
 **/
@Component
public class FirstCustomer {

    @RabbitListener(queues = {"first_queue"})
    public void message(String message, Channel channel) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("接受成功");
        System.out.println(message);
    }
}
