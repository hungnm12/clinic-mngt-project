package com.example.staffmngt.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ConfigValue {


    @Value(value = "${kafka.topic.name-staff-add-acc-req}")
    private String addAccTopic;


    @Value(value = "${kafka.pub.topic.send-deny-mail}")
    private String sendDenyMailTopic;

    @Value(value = "${kafka.pub.topic.send-report-med}")
    private String sendReport;


}
