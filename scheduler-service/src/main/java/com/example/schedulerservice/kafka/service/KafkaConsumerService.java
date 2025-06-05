package com.example.schedulerservice.kafka.service;


import com.example.schedulerservice.dto.req.MailInfoFromDrReqDto;
import com.example.schedulerservice.dto.req.SendReportMailReq;
import com.example.schedulerservice.service.Impl.MailFromDrService;
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
    private MailFromDrService mailFromDrService;

    @KafkaListener(topics = "${kafka.pub.topic.send-deny-mail}", groupId = "clinic-mngt-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void fetchSchedulerMsg(String event, Acknowledgment acknowledgment,
                                  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                                  @Header(KafkaHeaders.OFFSET) int offsets) throws JsonProcessingException {
        log.info("evernt {}", event);
        ObjectMapper objectMapper = new ObjectMapper();
        MailInfoFromDrReqDto addSchedulerReq = objectMapper.readValue(event, MailInfoFromDrReqDto.class);
        mailFromDrService.sendDenyMail(addSchedulerReq);
        acknowledgment.acknowledge();

    }

    @KafkaListener(topics = "${kafka.pub.topic.send-report-med}", groupId = "clinic-mngt-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void fetchMsg(String event, Acknowledgment acknowledgment,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                         @Header(KafkaHeaders.OFFSET) int offsets) throws JsonProcessingException {
        log.info("evernt {}", event);
        ObjectMapper objectMapper = new ObjectMapper();
        SendReportMailReq addSchedulerReq = objectMapper.readValue(event, SendReportMailReq.class);
        mailFromDrService.sendReportMail(addSchedulerReq);
        acknowledgment.acknowledge();

    }


}


