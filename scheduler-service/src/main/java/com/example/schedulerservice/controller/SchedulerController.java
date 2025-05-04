package com.example.schedulerservice.controller;


import com.example.schedulerservice.config.TenantContext;
import com.example.schedulerservice.dto.MultiTenantsEntity;
import com.example.schedulerservice.dto.req.AddSchedulerReq;
import com.example.schedulerservice.dto.res.GeneralResponse;
import com.example.schedulerservice.feign.TenantFeignClient;
import com.example.schedulerservice.service.Impl.ThymeLeafServiceImpl;
import com.example.schedulerservice.service.SchedulerService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

    private final SchedulerService schedulerService;
    private final ThymeLeafServiceImpl thymeLeafService;
    private final TenantFeignClient tenantFeignClient;

    public SchedulerController(SchedulerService schedulerService, ThymeLeafServiceImpl thymeLeafService, TenantFeignClient tenantFeignClient) {
        this.schedulerService = schedulerService;
        this.thymeLeafService = thymeLeafService;
        this.tenantFeignClient = tenantFeignClient;
    }


    @PostMapping("/addScheduler")
    GeneralResponse addScheduler(@RequestBody AddSchedulerReq addSchedulerReq, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);
        MultiTenantsEntity m = tenantFeignClient.getTenant(tenantId);
        String clinicAddress = m.getAddress();
        String clinicPhone = m.getPhone();

        buildMailInfor(addSchedulerReq, clinicAddress, clinicPhone);
        return schedulerService.addScheduler(addSchedulerReq);
    }

    private void buildMailInfor(AddSchedulerReq addSchedulerReq, String clinicAddress, String clinicPhone) {
        Map<String, Object> emailParams = Map.of(
                "patientName", addSchedulerReq.getPatientName(),
                "doctorName", addSchedulerReq.getDrName(),
                "appointmentDate", addSchedulerReq.getApmtDate() + " " + addSchedulerReq.getApmtTime(),
                "appointmentTime", addSchedulerReq.getApmtTime(),
                "clinicAddress", clinicAddress,
                "clinicPhone", clinicPhone);

        thymeLeafService.buildApointmentMail(emailParams);
    }
}
