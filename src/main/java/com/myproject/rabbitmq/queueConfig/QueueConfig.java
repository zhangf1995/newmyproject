package com.myproject.rabbitmq.queueConfig;

import com.myproject.rabbitmq.rabbitmqConfig.RabbitmqConfig;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

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

    //延迟队列,适用场景一:秒杀下单加入队列，一定时间内没有付款，进行业务操作
    @Bean
    public Queue queueDelay(){
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl",10000);
        arguments.put("x-dead-letter-exchange",RabbitmqConfig.DLX_EXCHANGE);
        arguments.put("x-dead-letter-routing-key",RabbitmqConfig.QUEUE_DLX);
        return new Queue("delay_queue",true,false,false,arguments);
    }

    @Bean
    public Queue dlxQueue(){
        return new Queue("dlx_queue",true,false,false);
    }
}
