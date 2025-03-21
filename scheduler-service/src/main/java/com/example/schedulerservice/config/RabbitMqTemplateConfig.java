package com.example.schedulerservice.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqTemplateConfig {


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange("");  // Default exchange
        rabbitTemplate.setRoutingKey("my_queue"); // Set default queue
        return rabbitTemplate;
    }
}
