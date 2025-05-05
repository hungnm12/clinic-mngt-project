package com.example.staffmngt.controller;

import com.example.staffmngt.configuration.TenantContext;
import com.example.staffmngt.dto.req.ListStaffSearchReq;
import com.example.staffmngt.dto.req.SignUpReqDto;
import com.example.staffmngt.dto.req.StaffReqDto;
import com.example.staffmngt.dto.req.UpdReqDto;
import com.example.staffmngt.dto.res.GeneralResponse;
import com.example.staffmngt.dto.res.StaffResDto;
import com.example.staffmngt.entity.StaffEntity;
import com.example.staffmngt.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;

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

    @GetMapping("/list")
    GeneralResponse listStaff(@RequestBody ListStaffSearchReq staffReqDto, @RequestHeader("X-Tenant-ID") String tenantId) {
        System.out.println("Received tenant ID: " + tenantId);
        TenantContext.setTenant(tenantId);
        return staffService.getListStaff(staffReqDto);

    }

    @GetMapping("/all")
    GeneralResponse getAllStaff() {
        return staffService.getAllStaff();
    }


}
