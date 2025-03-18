package com.example.multitenancyservice.controller;

import com.example.multitenancyservice.dto.req.AddTenantReq;
import com.example.multitenancyservice.entity.MultiTenantsEntity;
import com.example.multitenancyservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {
    @Autowired
    private TenantService tenantService;

    @GetMapping
    public ResponseEntity<List<MultiTenantsEntity>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<MultiTenantsEntity> getTenant(@PathVariable String tenantId) {
        return ResponseEntity.ok(tenantService.getTenantById(tenantId));
    }

    @PostMapping
    public ResponseEntity<MultiTenantsEntity> createTenant(@RequestBody AddTenantReq tenant) {
        return ResponseEntity.ok(tenantService.saveTenant(tenant));
    }

    @DeleteMapping("/{tenantId}")
    public ResponseEntity<Void> deleteTenant(@PathVariable String tenantId) {
        tenantService.deleteTenant(tenantId);
        return ResponseEntity.noContent().build();
    }
}