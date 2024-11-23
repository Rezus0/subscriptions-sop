package com.example.subscriptions_sop.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.golive.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.golive.name}")
    private String exchangeName;

    @Value("${rabbitmq.golive.key}")
    private String routingKey;

    @Bean
    public Queue goLiveQueue() {
        return new Queue(queueName, false);
    }

    @Bean
    public Exchange goLiveExchange() {
        return new TopicExchange(exchangeName, false ,false);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(goLiveQueue()).to(goLiveExchange()).with(routingKey).noargs();
    }

}
