package com.example.multitenancyservice.controller;

import com.example.multitenancyservice.dto.req.AddTenantReq;
import com.example.multitenancyservice.dto.res.TenantRes;
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
    public ResponseEntity<List<TenantRes>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    @GetMapping("/{clinicId}")
    public ResponseEntity<MultiTenantsEntity> getTenant(@PathVariable String clinicId) {
        return ResponseEntity.ok(tenantService.getTenantById(clinicId));
    }

    @PostMapping
    public ResponseEntity<MultiTenantsEntity> createTenant(@RequestBody AddTenantReq tenant) {
        return ResponseEntity.ok(tenantService.saveTenant(tenant));
    }

    @DeleteMapping("/{clinicId}")
    public ResponseEntity<Void> deleteTenant(@PathVariable String clinicId) {
        tenantService.deleteTenant(clinicId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping()
    public ResponseEntity<MultiTenantsEntity> editTenant(@RequestBody AddTenantReq tenant) {
        return ResponseEntity.ok(tenantService.editTenat(tenant));
    }
}