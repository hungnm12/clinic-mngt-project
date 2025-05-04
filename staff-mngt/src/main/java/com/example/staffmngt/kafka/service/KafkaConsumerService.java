package com.example.staffmngt.kafka.service;


import com.example.staffmngt.dto.res.KafkaMsgRes;
import com.example.staffmngt.dto.res.StaffResDto;
import com.example.staffmngt.dto.res.UserInfo;
import com.example.staffmngt.service.Impl.StaffServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @Autowired
    StaffServiceImpl staffService;


    @KafkaListener(topics = "${kafka.sub.topic.name-staff-add-acc-res}",
            groupId = "clinic-mngt-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void receiveMessage(String event, Acknowledgment acknowledgment,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                               @Header(KafkaHeaders.OFFSET) int offsets) throws JsonProcessingException {

        log.info("[subscribeEventMigrationStatus] [partition: {}, offset: {} | topic: {}] Received message: {}",
                partition, offsets, topic, event);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(event);
        JsonNode dataNode = jsonNode.path("data");

        System.out.println("Processing Staff Addition: " + jsonNode.get("data"));
        UserInfo userInfo = objectMapper.treeToValue(dataNode, UserInfo.class);
        // StaffResDto staffResDto = kafkaMsgRes.getData();
        staffService.processResponseStaffFromQueue(userInfo);
        acknowledgment.acknowledge();
    }
}


