package com.example.accountservice.controller;

import com.example.accountservice.config.JwtService;
import com.example.accountservice.config.TenantContext;
import com.example.accountservice.dto.req.AuthRequest;
import com.example.accountservice.dto.res.GeneralResponse;
import com.example.accountservice.entity.UserInfo;
import com.example.accountservice.repository.UserInfoRepository;
import com.example.accountservice.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserInfoService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserInfoRepository userInfoRepository;

    public UserController(@Qualifier("userInfoService") UserInfoService service, JwtService jwtService, AuthenticationManager authenticationManager, UserInfoRepository userInfoRepository) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userInfoRepository = userInfoRepository;
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
    public TokenDto authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        TenantContext.setTenant(authRequest.getTenantId());

        String username = authRequest.getUsername();
        String password = authRequest.getPassword();

        System.out.println("password = " + password + ", username = " + username);
        System.out.println("authRequest = " + authRequest);
        Optional<UserInfo> uio = userInfoRepository.findByEmail(authRequest.getUsername());
        if (userInfoRepository.findByEmailAndTenantId(authRequest.getUsername(), authRequest.getTenantId()).isEmpty()) {
            log.info("tao dang o day");
            return null;
        }
        String role = uio.map(UserInfo::getRoles).orElse(null);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return new TokenDto(jwtService.generateToken(authRequest.getUsername()), role, authRequest.getTenantId());

        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(
            @RequestHeader("Auth") String authHeader,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        System.out.println("authHeader = " + authHeader);
        TenantContext.setTenant(tenantId);

//        String token = authHeader.replace("Bearer ", "");
        boolean isValid = jwtService.validateToken(authHeader);
        return ResponseEntity.ok(isValid);
    }


    private class TokenDto {
        private String token;
        private String role;
        private String tenantId;

        public TokenDto(String token, String role, String tenantId) {
            this.token = token;
            this.role = role;
            this.tenantId = tenantId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public TokenDto() {
        }
    }

}