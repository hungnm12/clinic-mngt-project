package com.example.accountservice.rabbitmq;

import com.example.accountservice.constant.RoleEnum;
import com.example.accountservice.entity.UserInfo;
import com.example.accountservice.service.UserInfoService;
import com.example.accountservice.utils.ConvertUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class RabbitMqConsumer {


    private final UserInfoService service;

    public RabbitMqConsumer(@Qualifier("userInfoService") UserInfoService service) {
        this.service = service;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(String message) throws JsonProcessingException {
        log.info("Received message from queue: {}", message);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(message);

        String topic = jsonNode.path("topic").asText(null); // Use path() to avoid NullPointerException

        if ("add_acc".equals(topic)) {
            JsonNode dataNode = jsonNode.path("data");

            if (!dataNode.isMissingNode() && dataNode.isObject()) { // Ensure 'data' exists and is an object
                String email = dataNode.path("username").asText(null);
                String password = dataNode.path("password").asText(null);

                if (email != null && password != null) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setEmail(email);
                    userInfo.setPassword(new String(Base64.getDecoder().decode(password), StandardCharsets.UTF_8));
                    log.info("Decoded password: {}", userInfo.getPassword());
                    userInfo.setRoles(RoleEnum.USER_STAFF.toString());
                    service.addUser(userInfo);
                } else {
                    log.warn("Missing required fields in message: {}", message);
                }
            } else {
                log.warn("Invalid 'data' format in message: {}", message);
            }
        } else {
            log.info("Skipping message with topic: {}", topic);
        }
    }


}
