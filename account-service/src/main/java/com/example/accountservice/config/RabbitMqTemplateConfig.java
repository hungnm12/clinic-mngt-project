package com.example.accountservice.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
//public class RabbitMqTemplateConfig {
//    @Value("${rabbitmq.queue.name}")
//    private String queueName;
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setExchange("");
//        rabbitTemplate.setRoutingKey(queueName);
//        return rabbitTemplate;
//    }
//}
