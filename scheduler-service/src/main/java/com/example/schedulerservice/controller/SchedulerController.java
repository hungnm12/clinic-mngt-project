package com.example.schedulerservice.controller;


import com.example.schedulerservice.config.TenantContext;
import com.example.schedulerservice.dto.MultiTenantsEntity;
import com.example.schedulerservice.dto.req.AddSchedulerReq;
import com.example.schedulerservice.dto.req.MailInfoReqDto;
import com.example.schedulerservice.dto.res.GeneralResponse;
import com.example.schedulerservice.feign.TenantFeignClient;
import com.example.schedulerservice.service.Impl.ThymeLeafServiceImpl;
import com.example.schedulerservice.service.MailService;
import com.example.schedulerservice.service.SchedulerService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

    private final SchedulerService schedulerService;
    private final ThymeLeafServiceImpl thymeLeafService;
    private final TenantFeignClient tenantFeignClient;
    private final MailService mailService;

    public SchedulerController(SchedulerService schedulerService, ThymeLeafServiceImpl thymeLeafService, TenantFeignClient tenantFeignClient, MailService mailService) {
        this.schedulerService = schedulerService;
        this.thymeLeafService = thymeLeafService;
        this.tenantFeignClient = tenantFeignClient;
        this.mailService = mailService;
    }


    @PostMapping("/addScheduler")
    GeneralResponse addScheduler(@RequestBody AddSchedulerReq addSchedulerReq, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);
        MultiTenantsEntity m = tenantFeignClient.getTenant(tenantId);
        String clinicAddress = m.getAddress();
        String clinicPhone = m.getPhone();

        String data = buildMailInfor(addSchedulerReq, clinicAddress, clinicPhone);
        MailInfoReqDto mail = MailInfoReqDto.builder()
                .emailReceiver(addSchedulerReq.getPatientEmail())
                .subject("Appointment Confirmation")
                .contentMail(data)
                .attachFile(null)
                .build();
        try {
            mailService.sendMail(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedulerService.addScheduler(addSchedulerReq);
    }

    private String buildMailInfor(AddSchedulerReq addSchedulerReq, String clinicAddress, String clinicPhone) {
        Map<String, Object> emailParams = Map.of(
                "patientName", addSchedulerReq.getPatientName(),
                "doctorName", addSchedulerReq.getDrName(),
                "appointmentDate", addSchedulerReq.getApmtDate() + " " + addSchedulerReq.getApmtTime(),
                "appointmentTime", addSchedulerReq.getApmtTime(),
                "clinicAddress", clinicAddress,
                "clinicPhone", clinicPhone);

        return thymeLeafService.buildApointmentMail(emailParams);
    }


}
