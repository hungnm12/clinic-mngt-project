package com.example.staffmngt.configuration;

import com.example.staffmngt.dto.MultiTenantsEntity;
import com.example.staffmngt.feign.TenantFeignClient;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MultiTenantDataSource extends AbstractRoutingDataSource {
    private final Map<Object, Object> dataSources = new ConcurrentHashMap<>();

    @Lazy
    @Autowired
    private TenantFeignClient tenantFeignClient;

    @Value("${multitenancy.default.url}")
    private String defaultUrl;

    @Value("${multitenancy.default.username}")
    private String defaultUsername;

    @Value("${multitenancy.default.password}")
    private String defaultPassword;

    @Value("${multitenancy.default.driver}")
    private String defaultDriver;

    @PostConstruct
    public void init() {
        DataSource defaultDataSource = createDataSource(defaultUrl, defaultUsername, defaultPassword);

        dataSources.put("default", defaultDataSource);
        setTargetDataSources(new HashMap<>(dataSources));
        setDefaultTargetDataSource(defaultDataSource);
        afterPropertiesSet(); // Ensure the data sources are properly set up

        log.info("✅ Default tenant database initialized: {}", defaultUrl);
    }


    @Override
    protected Object determineCurrentLookupKey() {
        String tenantId = TenantContext.getTenant();
        log.info("🌍 Current Tenant: {}", tenantId);
        return (tenantId != null) ? tenantId : "default";
    }


    public void addTenantDataSource(String tenantId) {
        log.info("start with tenantId: {}", tenantId);
        if (!dataSources.containsKey(tenantId)) {
            MultiTenantsEntity tenant = tenantFeignClient.getTenant(tenantId);
            // 🔹 Debugging: Log the retrieved tenant details
            log.info("🔍 Retrieved Tenant Data: {}", tenant);
            // 🔹 Log the retrieved tenant details
            log.info("Retrieved Tenant Data: {}", tenant);

            if (tenant == null || tenant.getDbUrl() == null || tenant.getDbUrl().isEmpty()) {
                log.error("❌ Tenant database URL is missing for tenant: {}", tenantId);
                throw new IllegalArgumentException("Tenant database URL is missing!");
            }

            DataSource dataSource = createDataSource(tenant.getDbUrl(), tenant.getUsername(), tenant.getPassword());
            dataSources.put(tenantId, dataSource);
            updateTargetDataSources();
            initializeSchema(dataSource);
        }
    }


    private void updateTargetDataSources() {
        log.info("🔍 Current Target DataSources: {}", dataSources);
        setTargetDataSources(new HashMap<>(dataSources));
        afterPropertiesSet();
    }

    private DataSource createDataSource(String url, String username, String password) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("❌ JDBC URL is required for tenant data source!");
        }

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(defaultDriver);

        dataSource.setMinimumIdle(2);
        dataSource.setMaximumPoolSize(10);
        dataSource.setIdleTimeout(30000);
        dataSource.setConnectionTimeout(30000);
        dataSource.setMaxLifetime(1800000);

        return dataSource;
    }

    private void initializeSchema(DataSource dataSource) {
        String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS staff (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT, 
                        last_name VARCHAR(255),
                        first_name VARCHAR(255),
                        age INT,
                        role VARCHAR(255),
                        email VARCHAR(255) NOT NULL UNIQUE,
                        department VARCHAR(255),
                        staff_code VARCHAR(255) UNIQUE
                    )
                """;

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            log.error("Error initializing schema for tenant", e);
        }
    }

}

