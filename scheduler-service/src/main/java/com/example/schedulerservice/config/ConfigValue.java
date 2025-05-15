package com.example.schedulerservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ConfigValue {


    @Value(value = "${kafka.topic.name-pub-scheduler}")
    private String addScheduleTopic;

}
