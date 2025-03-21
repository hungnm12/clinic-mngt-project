package com.example.schedulerservice.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_HEADER = "X-Tenant-ID";
    @Autowired
    private MultiTenantDataSource multiTenantDataSource;

    @Override
    public boolean preHandle(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(TENANT_HEADER);
        if (tenantId != null && !tenantId.isEmpty()) {
            TenantContext.setTenant(tenantId);
            multiTenantDataSource.addTenantDataSource(tenantId);
        } else {
            TenantContext.clear();
        }
        return true;
    }
}