package com.example.accountservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ConfigValue {
//    @Value(value = "${kafka.nosql-credential.topic.name}")
//    private String noSqlCredentialTopicName;
//    @Value(value = "${kafka.sql-credential.topic.name}")
//    private String sqlCredentialTopicName;
//    @Value(value = "${kafka.status.topic.name}")
//    private String statusTopicName;
//    @Value(value = "${kafka.credential.topic.name}")
//    private String credentialTopicName;
    @Value(value = "${kafka.topic.name-staff-add-acc-res}")
    private String addAccTopic;

}
