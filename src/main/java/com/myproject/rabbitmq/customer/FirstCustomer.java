package com.myproject.rabbitmq.customer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @program: myproject
 * @description: 第一个消费者
 * @author: zf
 * @create: 2019-04-16 16:27
 **/
@Component
public class FirstCustomer {

    @RabbitListener(queues = {"first_queue","second_queue"})
    public void message(Message message, Channel channel) throws IOException {
        System.out.println("接受成功");
        System.out.println(new String(message.getBody()));
        //手动确认删除队列的消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        //手动确认不删除
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
    }

  /*  @RabbitListener(queues = {"delay_queue"})
    public void delayMessage(Message message,Channel channel) throws IOException {
        System.out.println("延迟成功");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }*/

  @RabbitListener(queues = {"dlx_queue"})
  public void dlxMessage(Message message,Channel channel) throws IOException {
      System.out.println("死信队列");
      System.out.println(new String(message.getBody()));
      channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
  }
}
