//package com.example.accountservice.rabbitmq;
//
//import com.example.accountservice.constant.RoleEnum;
//import com.example.accountservice.constant.StatusConstant;
//import com.example.accountservice.entity.UserInfo;
//import com.example.accountservice.service.UserInfoService;
//import com.example.accountservice.utils.ConvertUtils;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.Map;
//
//
//@Slf4j
//@Service
//public class RabbitMqConsumer {
//
//
//    private final UserInfoService service;
//
//    public RabbitMqConsumer(@Qualifier("userInfoService") UserInfoService service) {
//        this.service = service;
//    }
//
//    @RabbitListener(queues = "${rabbitmq.queue.name-staff-add-acc-req}")
//    public void receiveMessage(String message) throws JsonProcessingException {
//        log.info("Received message from queue: {}", message);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(message);
//        JsonNode dataNode = jsonNode.path("data");
//
//
//        switch (dataNode.path("status").asText()) {
//            case StatusConstant.PENDING: {
//                    String type = dataNode.path("type").asText();
//                    if ("Staff".equals(type) || "Patient".equals(type)) {
//                        if (!dataNode.isMissingNode() && dataNode.isObject()) { // Ensure 'data' exists and is an object
//                            String email = dataNode.path("username").asText(null);
//                            String password = dataNode.path("password").asText(null);
//                            String tenantId = dataNode.path("tenant-id").asText(null);
//                            if (email != null && password != null) {
//                                UserInfo userInfo = new UserInfo();
//                                userInfo.setEmail(email);
//                                userInfo.setPassword(new String(Base64.getDecoder().decode(password), StandardCharsets.UTF_8));
//                                userInfo.setTenantId(tenantId);
//                                userInfo.setType(type);
//                                log.info("Decoded password: {}", userInfo.getPassword());
//
//                                // Set role based on type
//                                if ("Staff".equals(type)) {
//                                    userInfo.setRoles(RoleEnum.USER_STAFF.toString());
//                                } else {
//                                    userInfo.setRoles(RoleEnum.USER_PATIETNT.toString());
//                                }
//
//                                service.addUser(userInfo);
//                            } else {
//                                log.warn("Missing required fields in message: {}", message);
//                            }
//                        } else {
//                            log.warn("Invalid 'data' format in message: {}", message);
//                        }
//                    } else {
//                        log.info("Skipping message with unsupported type: {}", type);
//                    }
//                break; // Add break to avoid fall-through
//            }
//
//            default:
//                log.info("Skipping message with status: {}", dataNode.path("status").asText());
//        }
//
//
//    }
//
//
//}
