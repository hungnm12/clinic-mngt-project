//package com.example.staffmngt.rabbitmq;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class RabbitMqProducer {
//    private final RabbitTemplate rabbitTemplate;
//
//
//    public RabbitMqProducer(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    public void sendMessage(String message, String queueName) {
//        log.info("Sending Message to RabbitMQ with queue {}", queueName);
//        rabbitTemplate.convertAndSend(queueName, message);
//
//    }
//
//    public void sendCreatedAccToAccSrv(String jsonMsg) {
//        log.info("[publish account creation rabbit]" + jsonMsg);
//        try {
//            sendMessage("rabbit-queue-staff-add-acc-req", jsonMsg);
//
//        } catch (Exception e) {
//            System.err.println("Error converting message to JSON: " + e.getMessage());
//        }
//
//    }
//}
