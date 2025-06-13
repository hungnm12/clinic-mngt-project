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

    @Value("${multitenancy.default.driver-class-name}")
    private String defaultDriver;
    // Department Table
    public static final String CREATE_DEPARTMENTS_TABLE = """
            CREATE TABLE IF NOT EXISTS departments (
                department_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL UNIQUE
            )
            """;

    private final String CREATE_SCHEDULER_TABLE_SQL = """
    CREATE TABLE scheduler (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        schedulerCode VARCHAR(255),
        patientName VARCHAR(255),
        patientTelephone VARCHAR(255),
        patientEmail VARCHAR(255),
        drName VARCHAR(255),
        orderedSrv VARCHAR(255),
        dateApmt TIMESTAMP WITH TIME ZONE,
        note TEXT,
        status VARCHAR(255)
    );
""";

    // Staff Table
    public static final String CREATE_STAFF_TABLE = """
            CREATE TABLE staff (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                lastName VARCHAR(255),
                firstName VARCHAR(255),
                age INT,
                role VARCHAR(255),
                email VARCHAR(255),
                phone VARCHAR(255),
                specialty VARCHAR(255),
                department_id BIGINT,
                staffCode VARCHAR(255),
                password VARCHAR(255),
                status VARCHAR(255),
                FOREIGN KEY (department_id) REFERENCES departments(department_id)
            )
            """;

    // Staff History Table
    public static final String CREATE_STAFF_HISTORY_TABLE = """
            CREATE TABLE staff_history (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                last_name VARCHAR(255),
                first_name VARCHAR(255),
                age INT,
                role VARCHAR(255),
                phone VARCHAR(255),
                specialty VARCHAR(255),
                email VARCHAR(255),
                department_id BIGINT,
                staffCode VARCHAR(255),
                password VARCHAR(255),
                status VARCHAR(255),
                FOREIGN KEY (department_id) REFERENCES departments(department_id)
            )
            """;

    // Services Table
    public static final String CREATE_SERVICES_TABLE = """
            CREATE TABLE services (
                service_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                service_name VARCHAR(255),
                service_code VARCHAR(255),
                note TEXT,
                price DOUBLE,
                department_id BIGINT,
                FOREIGN KEY (department_id) REFERENCES departments(department_id)
            )
            """;

    // Shift Schedules Table
    public static final String CREATE_SHIFT_SCHEDULES_TABLE = """
            CREATE TABLE shift_schedules (
                shift_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                shift_code VARCHAR(255),
                staff_id BIGINT NOT NULL,
                booked_patient VARCHAR(255),
                schedulerCode VARCHAR(255),
                booked_time TIMESTAMP,
                status VARCHAR(255),
                note TEXT,
                FOREIGN KEY (staff_id) REFERENCES staff(id)
            )
            """;

    // Record Table
    public static final String CREATE_RECORD_TABLE = """
            CREATE TABLE record (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                patientName VARCHAR(255),
                patientPhone VARCHAR(255),
                patientDob VARCHAR(255),
                patientEmail VARCHAR(255),
                serviceType VARCHAR(255),
                diagnose TEXT,
                assumption TEXT,
                symptom TEXT,
                assign VARCHAR(255),
                note TEXT,
                department_id BIGINT,
                staffCode VARCHAR(255),
                staffName VARCHAR(255),
                FOREIGN KEY (department_id) REFERENCES departments(department_id)
            )
            """;



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
                    CREATE TABLE IF NOT EXISTS user_info (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT, 
                        email VARCHAR(255),
                        name VARCHAR(255),
                        password VARCHAR(255),
                        roles VARCHAR(255),
                        status VARCHAR(255),
                        tenant_id VARCHAR(255),
                        type VARCHAR(255),
                        staffCode VARCHAR(255) UNIQUE
                    )
                    
                """;
        String createDepartmentsTable = "CREATE TABLE departments (\n" +
                "    department_id BIGINT,\n" +
                "    name VARCHAR(255)\n" +
                ");";

        String createLeaveRequestTable = "CREATE TABLE leave_request (\n" +
                "    leave_id BIGINT,\n" +
                "    staff_id BIGINT,\n" +
                "    startDate VARCHAR(255),\n" +
                "    endDate VARCHAR(255),\n" +
                "    leaveType VARCHAR(255),\n" +
                "    status VARCHAR(255)\n" +
                ");";

        String createRecordTable = "CREATE TABLE record (\n" +
                "    id BIGINT,\n" +
                "    patientName VARCHAR(255),\n" +
                "    patientPhone VARCHAR(255),\n" +
                "    patientDob VARCHAR(255),\n" +
                "    patientEmail VARCHAR(255),\n" +
                "    serviceType VARCHAR(255),\n" +
                "    diagnose VARCHAR(255),\n" +
                "    assumption VARCHAR(255),\n" +
                "    symptom VARCHAR(255),\n" +
                "    assign VARCHAR(255),\n" +
                "    note VARCHAR(255),\n" +
                "    department_id BIGINT,\n" +
                "    staffCode VARCHAR(255),\n" +
                "    staffName VARCHAR(255)\n" +
                ");";

        String createSalaryTable = "CREATE TABLE salaries (\n" +
                "    salary_id BIGINT,\n" +
                "    staff_id BIGINT,\n" +
                "    baseSalary DOUBLE,\n" +
                "    bonus DOUBLE,\n" +
                "    deductions DOUBLE,\n" +
                "    netSalary DOUBLE\n" +
                ");";

        String createServiceTable = "CREATE TABLE services (\n" +
                "    service_id BIGINT,\n" +
                "    service_name VARCHAR(255),\n" +
                "    service_code VARCHAR(255),\n" +
                "    note VARCHAR(255),\n" +
                "    price DOUBLE,\n" +
                "    department_id BIGINT\n" +
                ");";

        String createShiftScheduleTable = "CREATE TABLE shiftschedule (\n" +
                "    shift_id BIGINT,\n" +
                "    shiftCode VARCHAR(255),\n" +
                "    staff_id BIGINT,\n" +
                "    bookedPatient VARCHAR(255),\n" +
                "    schedulerCode VARCHAR(255),\n" +
                "    bookedTime TIMESTAMP,\n" +
                "    status VARCHAR(255),\n" +
                "    note VARCHAR(255)\n" +
                ");";

        String createStaffTable = "CREATE TABLE staff (\n" +
                "    id BIGINT,\n" +
                "    lastName VARCHAR(255),\n" +
                "    firstName VARCHAR(255),\n" +
                "    age INT,\n" +
                "    role VARCHAR(255),\n" +
                "    email VARCHAR(255),\n" +
                "    phone VARCHAR(255),\n" +
                "    specialty VARCHAR(255),\n" +
                "    department_id BIGINT,\n" +
                "    staffCode VARCHAR(255),\n" +
                "    password VARCHAR(255),\n" +
                "    status VARCHAR(255)\n" +
                ");";

        String createStaffHistoryTable = "CREATE TABLE staffhistory (\n" +
                "    id BIGINT,\n" +
                "    lastName VARCHAR(255),\n" +
                "    firstName VARCHAR(255),\n" +
                "    age INT,\n" +
                "    role VARCHAR(255),\n" +
                "    phone VARCHAR(255),\n" +
                "    specialty VARCHAR(255),\n" +
                "    email VARCHAR(255),\n" +
                "    department_id BIGINT,\n" +
                "    staffCode VARCHAR(255),\n" +
                "    password VARCHAR(255),\n" +
                "    status VARCHAR(255)\n" +
                ");";



        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
            statement.executeUpdate(CREATE_DEPARTMENTS_TABLE);
            statement.executeUpdate(CREATE_RECORD_TABLE);
            statement.executeUpdate(CREATE_STAFF_TABLE);
            statement.executeUpdate(CREATE_STAFF_HISTORY_TABLE);
            statement.executeUpdate(CREATE_SCHEDULER_TABLE_SQL);
            statement.executeUpdate(CREATE_SERVICES_TABLE);
            statement.executeUpdate(CREATE_SHIFT_SCHEDULES_TABLE);
        } catch (SQLException e) {
            log.error("Error initializing schema for tenant", e);
        }
    }

}

