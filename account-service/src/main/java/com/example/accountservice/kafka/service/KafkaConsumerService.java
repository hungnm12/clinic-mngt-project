package com.example.accountservice.kafka.service;


import com.example.accountservice.constant.RoleEnum;
import com.example.accountservice.constant.StatusConstant;
import com.example.accountservice.dto.res.GeneralResponse;
import com.example.accountservice.entity.UserInfo;
import com.example.accountservice.service.UserInfoService;
import com.example.accountservice.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@Slf4j
public class KafkaConsumerService {

    @Autowired
    private UserInfoService service;

    private final UserInfoService uService;

    public KafkaConsumerService(@Qualifier("userInfoService") UserInfoService uService) {
        this.uService = uService;
    }

    @KafkaListener(topics = "${kafka.sub.topic.name-staff-add-acc-req}",
            groupId = "clinic-mngt-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void subRegisterAcc(String event, Acknowledgment acknowledgment,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                               @Header(KafkaHeaders.OFFSET) int offsets) throws JsonProcessingException {

        log.info("[subscribeEventMigrationStatus] [partition: {}, offset: {} | topic: {}] Received message: {}",
                partition, offsets, topic, event);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(event);

        String status = jsonNode.path("status").asText();
        String type = jsonNode.path("type").asText();

        if (StatusConstant.PENDING.equalsIgnoreCase(status)) {
            if ("Staff".equals(type) || "Patient".equals(type)) {
                String email = jsonNode.path("username").asText(null);
                String password = jsonNode.path("password").asText(null);
                String tenantId = jsonNode.path("tenant-id").asText(null);
                String staffCode = jsonNode.path("staffCode").asText(null);

                if (email != null && password != null) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setStaffCode(staffCode);
                    userInfo.setEmail(email);
                    userInfo.setPassword(new String(Base64.getDecoder().decode(password), StandardCharsets.UTF_8));
                    userInfo.setTenantId(tenantId);
                    userInfo.setType(type);
                    log.info("Decoded password: {}", userInfo.getPassword());

                    // Set role based on type
                    if ("Staff".equals(type)) {
                        userInfo.setRoles(RoleEnum.USER_STAFF.toString());
                    } else {
                        userInfo.setRoles(RoleEnum.USER_PATIETNT.toString());
                    }

                    service.addUser(userInfo);
                } else {
                    log.warn("Missing required fields in message: {}", event);
                }
            } else {
                log.info("Skipping message with unsupported type: {}", type);
            }
        } else {
            log.info("Skipping message with status: {}", status);
        }

        acknowledgment.acknowledge();
    }

//
//    @KafkaListener(topics = "${kafka.sub.topic.name-staff-add-acc-req}", groupId = "clinic-mngt-service-group")
//    public void listen(String message) {
//        System.out.println("🔔 Received message: " + message);
//    }

}


