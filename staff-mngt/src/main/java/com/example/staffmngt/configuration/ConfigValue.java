package com.example.staffmngt.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ConfigValue {


    @Value(value = "${kafka.topic.name-staff-add-acc-req}")
    private String addAccTopic;

}
