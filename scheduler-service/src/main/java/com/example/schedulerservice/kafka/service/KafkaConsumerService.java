package com.example.schedulerservice.kafka.service;



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


    @KafkaListener(topics = "${}",
            groupId = "clinic-mngt-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void receiveMessage(String event, Acknowledgment acknowledgment,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                               @Header(KafkaHeaders.OFFSET) int offsets) throws JsonProcessingException {

        log.info("[subscribeEventMigrationStatus] [partition: {}, offset: {} | topic: {}] Received message: {}",
                partition, offsets, topic, event);

        acknowledgment.acknowledge();
    }
}


