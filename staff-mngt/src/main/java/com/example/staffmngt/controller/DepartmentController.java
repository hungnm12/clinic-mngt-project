package com.example.staffmngt.controller;


import com.example.staffmngt.configuration.TenantContext;
import com.example.staffmngt.dto.res.GeneralResponse;
import com.example.staffmngt.service.DepartmentService;
import com.example.staffmngt.service.ServiceClinicService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/add")
    GeneralResponse addDpmt(@RequestParam String name, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);

        return departmentService.addDepartment(name);
    }
}
