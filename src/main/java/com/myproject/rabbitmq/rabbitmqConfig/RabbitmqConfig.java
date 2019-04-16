package com.myproject.rabbitmq.rabbitmqConfig;

import com.myproject.rabbitmq.exchangeConfig.ExchangeConfig;
import com.myproject.rabbitmq.queueConfig.QueueConfig;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: myproject
 * @description: mq配置类
 * @author: zf
 * @create: 2019-04-16 11:30
 **/
@Configuration
public class RabbitmqConfig {

    //交换机名称
    public static final String EXCHANGE = "exchangeTest";

    //队列1
    public static final String QUEUE_ONE = "queue_one";

    //队列2
    public static final String QUEUE_TWO = "queue_two";

    @Autowired
    private ExchangeConfig exchangeConfig;
    @Autowired
    private QueueConfig queueConfig;
    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * 将消息队列1与交换器进行绑定
     */
    @Bean
    public Binding bindingQueueOne(){
        return BindingBuilder.bind(queueConfig.queueOne()).to(exchangeConfig.directExchange()).with(RabbitmqConfig.QUEUE_ONE);
    }

    /**
     * 将消息队列2与交换器进行绑定
     */
    @Bean
    public Binding bindingQueueTwo(){
        return BindingBuilder.bind(queueConfig.queueTwo()).to(exchangeConfig.directExchange()).with(RabbitmqConfig.QUEUE_TWO);
    }

    /**
     * 监听模式
     */

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer_one(){
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        Queue[] queues = {queueConfig.queueOne(), queueConfig.queueTwo()};
        simpleMessageListenerContainer.addQueues(queues);
        simpleMessageListenerContainer.setExposeListenerChannel(true);
        simpleMessageListenerContainer.setMaxConcurrentConsumers(5);
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        return simpleMessageListenerContainer;
    }

    /**
     * 定义rabbit template用于数据的接收和发送
     */
    @Bean
    public RabbitTemplate getRabbitTemplate(){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        /**若使用confirm-callback或return-callback，
         * 必须要配置publisherConfirms或publisherReturns为true
         * 每个rabbitTemplate只能有一个confirm-callback和return-callback
         */
        template.setConfirmCallback(msgSendConfirmCallBack());
        //template.setReturnCallback(msgSendReturnCallback());
        /**
         * 使用return-callback时必须设置mandatory为true，或者在配置中设置mandatory-expression的值为true，
         * 可针对每次请求的消息去确定’mandatory’的boolean值，
         * 只能在提供’return -callback’时使用，与mandatory互斥
         */
        //  template.setMandatory(true);
        return template;
    }


    /**
     * 消息确认机制
     * Confirms给客户端一种轻量级的方式，能够跟踪哪些消息被broker处理，
     * 哪些可能因为broker宕掉或者网络失败的情况而重新发布。
     * 确认并且保证消息被送达，提供了两种方式：发布确认和事务。(两者不可同时使用)
     * 在channel为事务时，不可引入确认模式；同样channel为确认模式下，不可使用事务。
     * @return
     */
    @Bean
    public MsgSendConfirmCallBack msgSendConfirmCallBack(){
        return new MsgSendConfirmCallBack();
    }

}
