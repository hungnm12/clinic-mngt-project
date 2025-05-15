package com.example.schedulerservice.feign;


import com.example.schedulerservice.dto.res.GeneralResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "staff-service", url = "http://localhost:8084")
public interface StaffFeignClient {

    @GetMapping("/isStaffExist")
    GeneralResponse isStaffExist(@RequestParam String drName, @RequestHeader("X-Tenant-ID") String tenantId);


    @GetMapping("/isSpecialtyExist")
    GeneralResponse isSpecialtyExist(@RequestParam String specialty, @RequestHeader("X-Tenant-ID") String tenantId);

}
