package com.example.staffmngt.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class RabbitMqConsumer {
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(message);

        String topic = jsonNode.get("topic").asText();

        if ("add_acc_res".equals(topic)) {
            System.out.println("Processing Staff Addition: " + jsonNode.get("data"));
            // Handle staff logic here
        } else {
            System.out.println("Skipping message: " + topic);
        }

    }
}
