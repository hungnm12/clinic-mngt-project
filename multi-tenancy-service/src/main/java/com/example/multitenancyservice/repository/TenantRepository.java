package com.example.multitenancyservice.repository;

import com.example.multitenancyservice.entity.MultiTenantsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TenantRepository extends JpaRepository<MultiTenantsEntity, Long> {
    Optional<MultiTenantsEntity> findByTenantId(String tenantId);
}
