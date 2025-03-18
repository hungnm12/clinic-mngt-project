package com.example.multitenancyservice.service;

import com.example.multitenancyservice.dto.req.SqlCredentialReq;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Service
@Transactional
@Slf4j
public class TestConnectionService {


    public Boolean validateSqlCredential(SqlCredentialReq req) {
        log.info("[validateSqlCredential] with input {}", req);
        try {
            String jdbcUrl = req.getUrl();
            try (Connection connection = DriverManager.getConnection(jdbcUrl, req.getUsername(), req.getPassword())) {
                log.info("[validateSqlCredential] connection established");

                return true;
            }

        } catch (SQLException e) {
            // Connection failed if exception is thrown
            log.error("[validateSqlCredential] error", e);
            log.info("invalid credential!");
            return false;
        }
    }
}