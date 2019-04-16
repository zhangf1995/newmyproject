package com.myproject.rabbitmq.exchangeConfig;

import com.myproject.rabbitmq.rabbitmqConfig.RabbitmqConfig;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: myproject
 * @description: 交换机配置
 * @author: zf
 * @create: 2019-04-16 11:23
 **/
@Configuration
public class ExchangeConfig {
    /**
     *   1.定义direct exchange，绑定queueTest
     *   2.durable="true" rabbitmq重启的时候不需要创建新的交换机
     *   3.direct交换器相对来说比较简单，匹配规则为：如果路由键匹配，消息就被投送到相关的队列
     *     fanout交换器中没有路由键的概念，他会把消息发送到所有绑定在此交换器上面的队列中。
     *     topic交换器你采用模糊匹配路由键的原则进行转发消息到队列中
     *   key: queue在该direct-exchange中的key值，当消息发送给direct-exchange中指定key为设置值时，
     *   消息将会转发给queue参数指定的消息队列
     */

    //direct交换器
    @Bean
    public DirectExchange directExchange(){
        DirectExchange directExchange = new DirectExchange(RabbitmqConfig.EXCHANGE, true, false);
        return directExchange;
    }

/*    //fanout交换器
    @Bean
    public FanoutExchange fanoutExchange(){
        return null;
    }*/
}
