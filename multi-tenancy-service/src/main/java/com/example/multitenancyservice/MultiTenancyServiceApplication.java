package com.example.multitenancyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MultiTenancyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiTenancyServiceApplication.class, args);
    }

}
