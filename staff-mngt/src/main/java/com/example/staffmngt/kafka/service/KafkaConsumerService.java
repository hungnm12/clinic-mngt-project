package com.example.staffmngt.kafka.service;


import com.example.staffmngt.configuration.TenantContext;
import com.example.staffmngt.dto.req.AddSchedulerReq;
import com.example.staffmngt.dto.res.KafkaMsgRes;
import com.example.staffmngt.dto.res.StaffResDto;
import com.example.staffmngt.dto.res.UserInfo;
import com.example.staffmngt.service.Impl.ShiftScheduleServiceImpl;
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
    @Autowired
    ShiftScheduleServiceImpl shiftScheduleService;


    @KafkaListener(topics = "${kafka.sub.topic.name-staff-add-acc-res}",
            groupId = "clinic-mngt-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void receiveMessage(String event, Acknowledgment acknowledgment,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                               @Header(KafkaHeaders.OFFSET) int offsets
    ) throws JsonProcessingException {

        log.info("[subscribeEventMigrationStatus] [partition: {}, offset: {} | topic: {}] Received message: {}",
                partition, offsets, topic, event);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(event);
        JsonNode dataNode = jsonNode.path("data");
        JsonNode tenantNode = jsonNode.path("tenantId");
        log.info("hehe " + tenantNode);
        TenantContext.setTenant(objectMapper.treeToValue(tenantNode, String.class));
        System.out.println("Processing Staff Addition: " + jsonNode.get("data"));
        UserInfo userInfo = objectMapper.treeToValue(dataNode, UserInfo.class);
        // StaffResDto staffResDto = kafkaMsgRes.getData();
        staffService.processResponseStaffFromQueue(userInfo);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "${kafka.topic.name-pub-scheduler}", groupId = "clinic-mngt-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void fetchSchedulerMsg(String event, Acknowledgment acknowledgment,
                                  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                                  @Header(KafkaHeaders.OFFSET) int offsets) throws JsonProcessingException {
        log.info("evernt {}", event);
        ObjectMapper objectMapper = new ObjectMapper();
        AddSchedulerReq addSchedulerReq = objectMapper.readValue(event, AddSchedulerReq.class);
        shiftScheduleService.processShiftSchedule(addSchedulerReq);
        acknowledgment.acknowledge();

    }
}


