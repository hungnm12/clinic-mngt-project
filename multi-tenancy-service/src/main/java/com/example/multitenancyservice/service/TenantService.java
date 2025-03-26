package com.example.multitenancyservice.service;

import com.example.multitenancyservice.config.TenantNotFoundException;
import com.example.multitenancyservice.dto.req.AddTenantReq;
import com.example.multitenancyservice.dto.req.SqlCredentialReq;
import com.example.multitenancyservice.entity.MultiTenantsEntity;
import com.example.multitenancyservice.repository.TenantRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
@Transactional
public class TenantService {
    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TestConnectionService testConnectionService;

    public List<MultiTenantsEntity> getAllTenants() {
        return tenantRepository.findAll();
    }

    public MultiTenantsEntity getTenantById(String tenantId) {
        return tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found: " + tenantId));
    }

    public MultiTenantsEntity saveTenant(AddTenantReq tenantReq) {
        String clinicCode = "Clinic_" + tenantReq.getClinicName() + new Random().nextInt(100);
        MultiTenantsEntity tenant = MultiTenantsEntity
                .builder()
                .tenantId(tenantReq.getTenantId())
                .dbUrl(tenantReq.getDbUrl())
                .username(tenantReq.getUsername())
                .password(tenantReq.getPassword())
                .clinicCode(clinicCode)
                .address(tenantReq.getAddress())
                .drPhone(tenantReq.getDrPhone())
                .website(tenantReq.getWebsite())
                .drPhone(tenantReq.getDrPhone())
                .clinicEmail(tenantReq.getClinicEmail())
                .inchargedDr(tenantReq.getInchargedDr())
                .build();
        SqlCredentialReq sqlCredentialReq = new SqlCredentialReq();
        sqlCredentialReq.setUsername(tenantReq.getUsername());
        sqlCredentialReq.setPassword(tenantReq.getPassword());
        sqlCredentialReq.setUrl(tenantReq.getDbUrl());

        if (!testConnectionService.validateSqlCredential(sqlCredentialReq)) {
            log.error("SQL Credential validation failed");
            return null;
        }


        return tenantRepository.save(tenant);
    }

    public void deleteTenant(String tenantId) {
        MultiTenantsEntity tenant = getTenantById(tenantId);
        tenantRepository.delete(tenant);
    }
}
