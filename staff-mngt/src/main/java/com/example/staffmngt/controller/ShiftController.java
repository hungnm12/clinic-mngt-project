package com.example.staffmngt.controller;


import com.example.staffmngt.configuration.TenantContext;
import com.example.staffmngt.dto.req.AddShiftReq;
import com.example.staffmngt.dto.req.ListStaffSearchReq;
import com.example.staffmngt.dto.req.StaffReqDto;
import com.example.staffmngt.dto.req.UpdReqDto;
import com.example.staffmngt.dto.res.GeneralResponse;
import com.example.staffmngt.service.ShiftScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shift")
public class ShiftController {

    @Autowired
    private ShiftScheduleService shiftScheduleService;

    @DeleteMapping("/delete")
    GeneralResponse deleteStaff(@RequestParam String shiftCode, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);

        return shiftScheduleService.deleteShift(shiftCode);
    }

    @PostMapping("/update")
    GeneralResponse updateStaff(@RequestBody AddShiftReq shiftSchedule, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);
        return shiftScheduleService.updateShift(shiftSchedule);
    }

    @PostMapping("/add")
    GeneralResponse addStaff(@RequestBody AddShiftReq shiftSchedule, @RequestHeader("X-Tenant-ID") String tenantId) {
        System.out.println("Received tenant ID: " + tenantId);
        TenantContext.setTenant(tenantId);

        return shiftScheduleService.createShift(shiftSchedule);
    }


    @GetMapping("/all")
    GeneralResponse getAllStaff() {
        return shiftScheduleService.getAllShifts();
    }
}
