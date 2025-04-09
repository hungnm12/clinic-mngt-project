package com.example.multitenancyservice.service;

import com.example.multitenancyservice.config.TenantNotFoundException;
import com.example.multitenancyservice.dto.req.AddTenantReq;
import com.example.multitenancyservice.dto.req.SqlCredentialReq;
import com.example.multitenancyservice.dto.res.TenantRes;
import com.example.multitenancyservice.entity.MultiTenantsEntity;
import com.example.multitenancyservice.repository.TenantRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@Transactional
public class TenantService {
    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TestConnectionService testConnectionService;

    public List<TenantRes> getAllTenants() {
        Map<Object, Object> map = new HashMap<>();
        List<MultiTenantsEntity> tenants = tenantRepository.findAll();
        List<TenantRes> t = new ArrayList<>();
        tenants.forEach(tenant -> {
            TenantRes res = new TenantRes();
            res.setTenantId(tenant.getTenantId());
            res.setAddress(tenant.getAddress());
            res.setEmail(tenant.getEmail());
            res.setPhone(tenant.getPhone());
            res.setClinicCode(tenant.getClinicCode());
            res.setClinicName(tenant.getClinicName());
            res.setWebsite(tenant.getWebsite());
            res.setDbUrl(tenant.getDbUrl());
            res.setUsername(tenant.getUsername());
            res.setPassword(tenant.getPassword());
            res.setRepresentativeName(tenant.getRepresentativeName());
            res.setSchemaName(tenant.getSchemaName());
            t.add(res);

        });
        return t;
    }

    public MultiTenantsEntity getTenantById(String clinicId) {
        return tenantRepository.findByClinicCode(clinicId);


    }

    public MultiTenantsEntity saveTenant(AddTenantReq tenantReq) {
        log.info("Saving tenant ");
        String clinicCode = "Clinic_" + tenantReq.getClinicName() + new Random().nextInt(100);
        MultiTenantsEntity tenant = MultiTenantsEntity
                .builder()
                .tenantId(tenantReq.getTenantId())
                .dbUrl(tenantReq.getDbUrl())
                .username(tenantReq.getUsername())
                .password(tenantReq.getPassword())
                .clinicCode(clinicCode)
                .address(tenantReq.getAddress())
                .schemaName(tenantReq.getSchemaName())
                .website(tenantReq.getWebsite())
                .phone(tenantReq.getPhone())
                .email(tenantReq.getEmail())
                .representativeName(tenantReq.getRepresentativeName())
                .clinicName(tenantReq.getClinicName())
                .build();
        SqlCredentialReq sqlCredentialReq = new SqlCredentialReq();
        sqlCredentialReq.setUsername(tenantReq.getUsername());
        sqlCredentialReq.setPassword(tenantReq.getPassword());
        sqlCredentialReq.setUrl(tenantReq.getDbUrl());
        log.info("output tenant {}" + tenant);
        if (!testConnectionService.validateSqlCredential(sqlCredentialReq)) {
            log.error("SQL Credential validation failed");
            return null;
        }


        return tenantRepository.save(tenant);
    }

    public MultiTenantsEntity editTenat(AddTenantReq tenantReq) {
        if (tenantReq.getTenantId() == null) {
            return null;
        }
        if (tenantRepository.findByClinicCode(tenantReq.getClinicCode()) == null) {
            return null;
        }
        MultiTenantsEntity t = tenantRepository.findByClinicCode(tenantReq.getClinicCode());
            t.setAddress(tenantReq.getAddress());
            t.setEmail(tenantReq.getEmail());
            t.setPhone(tenantReq.getPhone());
            t.setClinicName(tenantReq.getClinicName());
            t.setSchemaName(tenantReq.getSchemaName());
            t.setRepresentativeName(tenantReq.getRepresentativeName());
            t.setWebsite(tenantReq.getWebsite());
            t.setUsername(tenantReq.getUsername());
            t.setPassword(tenantReq.getPassword());
            t.setDbUrl(tenantReq.getDbUrl());
        SqlCredentialReq sqlCredentialReq = new SqlCredentialReq();
        sqlCredentialReq.setUsername(tenantReq.getUsername());
        sqlCredentialReq.setPassword(tenantReq.getPassword());
        sqlCredentialReq.setUrl(tenantReq.getDbUrl());
        log.info("output tenant {}" + t);
        if (!testConnectionService.validateSqlCredential(sqlCredentialReq)) {
            log.error("SQL Credential validation failed");
            return null;
        }


        return tenantRepository.save(t);
    }

    public void deleteTenant(String clinicId) {
        MultiTenantsEntity tenant = getTenantById(clinicId);
        tenantRepository.delete(tenant);
    }
}
