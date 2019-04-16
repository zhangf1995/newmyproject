package com.myproject.rabbitmq.queueConfig;

import com.myproject.rabbitmq.rabbitmqConfig.RabbitmqConfig;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: myproject
 * @description: 队列配置类
 * @author: zf
 * @create: 2019-04-16 11:30
 **/
@Configuration
public class QueueConfig {

    /**
     durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
     auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
     exclusive  表示该消息队列是否只在当前connection生效,默认是false
     */

    @Bean
    public Queue queueOne(){
        return new Queue("first_queue",true,false,false);
    }

    @Bean
    public Queue queueTwo(){
        return new Queue("second_queue",true,false,false);
    }
}
