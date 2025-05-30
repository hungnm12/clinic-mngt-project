package com.example.accountservice.feign;


import com.example.accountservice.dto.MultiTenantsEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "multi-tenancy-service", url = "http://localhost:8081")
public interface TenantFeignClient {
    @GetMapping("/api/tenants/{tenantId}")
    MultiTenantsEntity getTenant(@PathVariable String tenantId);
}

