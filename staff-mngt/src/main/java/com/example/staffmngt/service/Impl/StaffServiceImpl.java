package com.example.staffmngt.service.Impl;

import com.example.staffmngt.configuration.TenantContext;
import com.example.staffmngt.dto.req.SignUpReqDto;
import com.example.staffmngt.dto.req.StaffReqDto;
import com.example.staffmngt.dto.req.UpdReqDto;
import com.example.staffmngt.dto.res.GeneralResponse;
import com.example.staffmngt.dto.res.StaffResDto;
import com.example.staffmngt.entity.StaffEntity;
import com.example.staffmngt.enumSt.Role;
import com.example.staffmngt.repository.StaffEntityRepository;
import com.example.staffmngt.service.StaffService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Service
public class StaffServiceImpl implements StaffService {
    @Autowired
    private StaffEntityRepository staffEntityRepository;

    @Override
    public GeneralResponse addStaff(StaffReqDto staff) {
        log.info("Adding staff: {}", staff);

        if (staff == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff object is null, cannot proceed.", null);
        }

        String tenantId = TenantContext.getTenant();
        if (tenantId == null || tenantId.isEmpty()) {
            log.error("[addStaff] Tenant ID is missing!");
            throw new IllegalStateException("Tenant ID is missing!");
        }
        log.info("[addStaff] Tenant ID: {}", tenantId);

        String staffCode = "Staff_" + new Random().nextInt(100);
        if (staffEntityRepository.findByEmail(staff.getEmail()) != null) {
            return new GeneralResponse(HttpStatus.CONFLICT.value(), "", "Staff already exists!", null);
        }
        StaffEntity staff1 = StaffEntity.builder()
                .email(staff.getEmail())
                .lastName(staff.getLastName())
                .firstName(staff.getFirstName())
                .staffCode(staffCode)
                .age(staff.getAge())
                .department(staff.getDepartment())
                .role(String.valueOf(Role.EMPLOYEE))
                .build();

        log.info("Attempting to save staff: {}", staff1);

        try {
            staffEntityRepository.save(staff1);
            log.info("Staff saved successfully.");

            StaffEntity savedStaff = staffEntityRepository.findByStaffCode(staffCode);
            if (savedStaff == null) {
                log.error("Staff not found after saving! Possible multi-tenancy issue.");
                return new GeneralResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "", "Failed to save staff.", null);
            }

            return new GeneralResponse(HttpStatus.CREATED.value(), "", "Staff saved.", savedStaff);
        } catch (Exception e) {
            log.error("Error saving staff: ", e);
            return new GeneralResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "", "Error occurred while saving staff.", null);
        }
    }

    @Override
    public GeneralResponse updateStaff(UpdReqDto staff) {
        if (staff == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff object is null, cannot proceed.", null);
        }
        String tenantId = TenantContext.getTenant();
        if (tenantId == null) {
            log.info("[updateStaff] tenantId = null", tenantId);
            throw new IllegalStateException("Tenant ID is missing!");
        }
        StaffEntity st = staffEntityRepository.findByStaffCode(staff.getStaffCode());
        if (st == null) {
            return null;
        }
        StaffEntity updStff = StaffEntity.builder()
                .email(staff.getEmail())
                .lastName(staff.getLastName())
                .firstName(staff.getFirstName())
                .staffCode(st.getStaffCode())
                .age(staff.getAge())
                .department(staff.getDepartment())
                .role(st.getRole())
                .build();

        Map<Object, Object> map = new HashMap<>();
        map.put(updStff, StaffResDto.class);
        return new GeneralResponse(HttpStatus.OK.value(), "", "Staff updated", updStff);
    }

    @Override
    public GeneralResponse deleteStaff(String staffCode) {
        if (staffCode == null) {
            return new GeneralResponse();
        }

        String tenantId = TenantContext.getTenant();
        if (tenantId == null) {
            log.info("[deleteStaff] tenantId = null", tenantId);
            throw new IllegalStateException("Tenant ID is missing!");
        }
        StaffEntity st = staffEntityRepository.findByStaffCode(staffCode);
        if (st == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff object is null, cannot proceed.", null);
        }
        staffEntityRepository.delete(st);
        return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff deleted", null);

    }


    @Override
    public GeneralResponse searchStaff(UpdReqDto updReqDto) {
        if (updReqDto == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff object is null, cannot proceed.", null);
        }
        if (updReqDto.getStaffCode() == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "Staff object is null, cannot proceed.", null);
        }
        String tenantId = TenantContext.getTenant();
        if (tenantId == null) {
            log.info("[searchStaff] tenantId = null", tenantId);
            throw new IllegalStateException("Tenant ID is missing!");
        }
        List<StaffEntity> lstStff = staffEntityRepository.searchStaff(updReqDto.getStaffCode(),
                updReqDto.getFirstName(), updReqDto.getEmail(), updReqDto.getLastName(),
                updReqDto.getRole(), updReqDto.getDepartment());

        return new GeneralResponse(HttpStatus.OK.value(), "", "Staff searched", lstStff);
    }

//    private boolean isValidEmail(String email) {
//        if (email == null || email.length() < 5) {
//            return false;
//        }
//        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
//        Matcher matcher = emailPattern.matcher(email);
//        return matcher.find();
//    }
//
//    private boolean isValidPassword(String password) {
//        if (password == null || password.length() < 8) {
//            return false;
//        }
//        boolean hasUppercase = false;
//        boolean hasLowercase = false;
//        boolean hasSpecialChar = false;
//
//        Pattern uppercasePattern = Pattern.compile("[A-Z]");
//        Pattern lowercasePattern = Pattern.compile("[a-z]");
//        Pattern specialCharPattern = Pattern.compile("[^a-zA-Z0-9]");
//        Matcher uppercaseMatcher = uppercasePattern.matcher(password);
//        Matcher lowercaseMatcher = lowercasePattern.matcher(password);
//        Matcher specialCharMatcher = specialCharPattern.matcher(password);
//
//
//        hasUppercase = uppercaseMatcher.find();
//        hasLowercase = lowercaseMatcher.find();
//        hasSpecialChar = specialCharMatcher.find();
//
//        return hasUppercase && hasLowercase && hasSpecialChar;
//
//    }
}
