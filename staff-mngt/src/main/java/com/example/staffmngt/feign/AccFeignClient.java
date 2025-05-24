package com.example.staffmngt.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "account-service", url = "http://localhost:8089")
public interface AccFeignClient {
    @GetMapping("/auth/validate")
    ResponseEntity<Boolean> validateToken(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader("X-Tenant-ID") String tenantId);
}
