package com.example.accountservice.controller;

import com.example.accountservice.config.JwtService;
import com.example.accountservice.config.TenantContext;
import com.example.accountservice.dto.req.AuthRequest;
import com.example.accountservice.dto.res.GeneralResponse;
import com.example.accountservice.entity.UserInfo;
import com.example.accountservice.service.UserInfoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserInfoService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(@Qualifier("userInfoService") UserInfoService service, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/welcome")
    public String welcome(@RequestHeader(value = "X-Tenant-ID", required = false) String tenantId) {
        if (tenantId != null) {
            TenantContext.setTenant(tenantId);
        }
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public GeneralResponse addNewUser(@RequestBody UserInfo userInfo, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);
        return service.addUser(userInfo);
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}