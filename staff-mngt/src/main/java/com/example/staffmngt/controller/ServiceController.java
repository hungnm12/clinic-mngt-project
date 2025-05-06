package com.example.staffmngt.controller;


import com.example.staffmngt.configuration.TenantContext;
import com.example.staffmngt.dto.req.AddServiceReq;

import com.example.staffmngt.dto.req.UpdReqDto;
import com.example.staffmngt.dto.res.GeneralResponse;
import com.example.staffmngt.service.ServiceClinicService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service")
public class ServiceController {

    private final ServiceClinicService serviceClinicService;

    public ServiceController(ServiceClinicService serviceClinicService) {
        this.serviceClinicService = serviceClinicService;
    }

    @DeleteMapping("/delete")
    GeneralResponse deleteService(@RequestParam String serviceName, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);

        return serviceClinicService.deleteService(serviceName);
    }

    @PostMapping("/update")
    GeneralResponse updateService(@RequestBody AddServiceReq addServiceReq, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);
        return serviceClinicService.updateService(addServiceReq);
    }

    @PostMapping("/add")
    GeneralResponse addService(@RequestBody AddServiceReq addServiceReq, @RequestHeader("X-Tenant-ID") String tenantId) {
        System.out.println("Received tenant ID: " + tenantId);
        TenantContext.setTenant(tenantId);

        return serviceClinicService.addService(addServiceReq);
    }

    @PostMapping("/list")
    GeneralResponse getListService(@RequestBody AddServiceReq addServiceReq, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);
        return serviceClinicService.getListServices(addServiceReq);
    }

    @GetMapping("/all")
    GeneralResponse getAllServices() {
        return serviceClinicService.getAllServices();
    }

}
