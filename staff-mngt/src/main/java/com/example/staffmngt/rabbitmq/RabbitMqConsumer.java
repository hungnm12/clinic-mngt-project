package com.example.staffmngt.rabbitmq;

import com.example.staffmngt.dto.req.RabbitMsgReq;
import com.example.staffmngt.service.Impl.StaffServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class RabbitMqConsumer {

    private final StaffServiceImpl staffService;

    public RabbitMqConsumer(StaffServiceImpl staffService) {
        this.staffService = staffService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(message);
        JsonNode dataNode = jsonNode.path("data");
        String topic = jsonNode.get("topic").asText();

        if ("add_acc_res".equals(topic)) {
            System.out.println("Processing Staff Addition: " + jsonNode.get("data"));
            RabbitMsgReq rabbitMsgReq = objectMapper.treeToValue(dataNode, RabbitMsgReq.class);
            staffService.processResponseStaffFromQueue(rabbitMsgReq);
        } else {
            System.out.println("Skipping message: " + topic);
        }

    }
}
