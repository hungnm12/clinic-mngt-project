package com.example.staffmngt.controller;

import com.example.staffmngt.configuration.TenantContext;
import com.example.staffmngt.dto.req.ListStaffSearchReq;
import com.example.staffmngt.dto.req.SignUpReqDto;
import com.example.staffmngt.dto.req.StaffReqDto;
import com.example.staffmngt.dto.req.UpdReqDto;
import com.example.staffmngt.dto.res.GeneralResponse;
import com.example.staffmngt.dto.res.StaffResDto;
import com.example.staffmngt.entity.StaffEntity;
import com.example.staffmngt.feign.AccFeignClient;
import com.example.staffmngt.repository.StaffEntityRepository;
import com.example.staffmngt.service.StaffService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;
    @Autowired
    private StaffEntityRepository staffEntityRepository;
    @Autowired
    private AccFeignClient accFeignClient;

    @DeleteMapping("/delete")
    GeneralResponse deleteStaff(@RequestParam String staffCode, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);

        return staffService.deleteStaff(staffCode);
    }

    @PostMapping("/update")
    GeneralResponse updateStaff(@RequestBody UpdReqDto staff, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);
        return staffService.updateStaff(staff);
    }

    @PostMapping("/add")
    GeneralResponse addStaff(@RequestBody StaffReqDto staff, @RequestHeader("X-Tenant-ID") String tenantId) {
        System.out.println("Received tenant ID: " + tenantId);
        TenantContext.setTenant(tenantId);

        return staffService.addStaff(staff);
    }

    @PostMapping("/search")
    GeneralResponse searchStaff(@RequestBody UpdReqDto updReqDto) {
        return staffService.searchStaff(updReqDto);
    }

    @PostMapping("/list")
    GeneralResponse listStaff(@RequestBody ListStaffSearchReq staffReqDto, @RequestHeader("X-Tenant-ID") String tenantId) {
        System.out.println("Received tenant ID: " + tenantId);
        TenantContext.setTenant(tenantId);
        return staffService.getListStaff(staffReqDto);
    }

    @GetMapping("/all")
    GeneralResponse getAllStaff(@RequestHeader("X-Tenant-ID") String tenantId) {
        System.out.println("Received tenant ID: " + tenantId);
        TenantContext.setTenant(tenantId);
        return staffService.getAllStaff();
    }

    @GetMapping("/{staffCode}")
    GeneralResponse getStaffByStaffCode(@PathVariable String staffCode, @RequestHeader("X-Tenant-ID") String tenantId) {
        System.out.println("Received tenant ID: " + tenantId);
        TenantContext.setTenant(tenantId);
        return staffService.getStaff(staffCode);
    }

    @GetMapping("/getDrBySpecialty")
    GeneralResponse getDrBySpecialty(@RequestParam String specialty, @RequestHeader("X-Tenant-ID") String tenantId) {
        System.out.println("Received tenant ID: " + tenantId);
        TenantContext.setTenant(tenantId);
        return staffService.getDrBySpecialty(specialty);
    }

    @GetMapping("/getSpecialtyByDr")
    GeneralResponse getSpecialtyByDr(@RequestParam String staffCode, @RequestHeader("X-Tenant-ID") String tenantId) {
        System.out.println("Received tenant ID: " + tenantId);
        TenantContext.setTenant(tenantId);
        return staffService.getSpecialtyByDr(staffCode);
    }

    @GetMapping("/isStaffExist")
    GeneralResponse isStaffExist(@RequestParam String drName, @RequestHeader("X-Tenant-ID") String tenantId) {
        System.out.println("Received tenant ID: " + tenantId);
        TenantContext.setTenant(tenantId);
        return staffService.isStaffExist(drName);
    }

    @GetMapping("/isSpecialtyExist")
    GeneralResponse isSpecialtyExist(@RequestParam String specialty, @RequestHeader("X-Tenant-ID") String tenantId) {
        System.out.println("Received tenant ID: " + tenantId);
        TenantContext.setTenant(tenantId);
        return staffService.isSpecialtyExist(specialty);
    }

    @GetMapping("/getDrName")
    String getDrName(@RequestHeader("X-Tenant-ID") String tenantId, @RequestHeader("Authorization") String authHeader) {

        ResponseEntity<Boolean> re = accFeignClient.validateToken(authHeader, tenantId);
        if (!re.getStatusCode().is2xxSuccessful()) {
            return null;
        }

        String token = authHeader.replace("Bearer ", "");
        String email = extractUsername(token);
        StaffEntity st = staffEntityRepository.findByEmail(email);
        if (st == null) {
            return null;
        }
        return st.getFirstName() + " " + st.getLastName();
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode("hMGX8YeJ0hzRrB6QEzfqT2J94VKL3zQc/Mj2RVJyXzc=");
        // Ensure we have a key with at least 256 bits (32 bytes) for HS256
        if (keyBytes.length < 32) {
            throw new RuntimeException("JWT secret key must be at least 256 bits (32 bytes) when decoded from Base64. Current key is only " + (keyBytes.length * 8) + " bits.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}
