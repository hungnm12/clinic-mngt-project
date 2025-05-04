package com.example.staffmngt.kafka.service;


import com.example.staffmngt.configuration.ConfigValue;
import com.example.staffmngt.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ConfigValue configValue;


    private void sendMsgDefault(String topic, String data, String functionName)
            throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(topic, data);
        try {
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info(functionName + " [On success] Publish event send: " + data + " ***** topic: " + topic + " ***** with offset: " + result.getRecordMetadata().offset());
                } else {
                    log.error(functionName + " [On failed] Unable to publish event send inner: " + data, ex);
                }
            });
        } catch (Exception ex) {
            log.error(functionName + " [On failed] Unable to publish event send outer: " + data, ex);

        }

        SendResult<String, String> result = future.get(60, TimeUnit.SECONDS);
        if (result == null)
            log.error("send request failed");
    }


    public void sendAddAccReq(String msg) throws ExecutionException, InterruptedException, TimeoutException {
        log.info("[sendNoSqlToSqlCredentials] send credential request {}", msg);


        sendMsgDefault(configValue.getAddAccTopic(), msg, "[sendAddAccReqToAccSrv]");


    }

}
