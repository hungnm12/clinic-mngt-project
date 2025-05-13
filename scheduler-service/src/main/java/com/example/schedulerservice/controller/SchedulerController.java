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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {
    private static final ZoneId POLICY_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
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
    GeneralResponse addScheduler(@RequestBody AddSchedulerReq addSchedulerReq, @RequestHeader("X-Tenant-ID") String tenantId) throws ParseException {
        TenantContext.setTenant(tenantId);
        MultiTenantsEntity m = tenantFeignClient.getTenant(tenantId);
        String clinicAddress = m.getAddress();
        String clinicPhone = m.getPhone();

        // Call the scheduler service first and check response
        GeneralResponse response = schedulerService.addScheduler(addSchedulerReq);

        // If response code is 200, send the email
        if (response.getCode() == 200) {
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
                e.printStackTrace(); // Consider logging this instead of just printing
            }
        }

        return response;
    }

    private String buildMailInfor(AddSchedulerReq addSchedulerReq, String clinicAddress, String clinicPhone) {
        Map<String, Object> emailParams = Map.of(
                "patientName", addSchedulerReq.getPatientName(),
                "doctorName", addSchedulerReq.getDrName(),
                "appointmentDate", convertToLocalDate(String.valueOf(addSchedulerReq.getApmtDate())) + " " + convertToLocalTime(String.valueOf(addSchedulerReq.getApmtTime())),
                "appointmentTime", addSchedulerReq.getApmtTime(),
                "clinicAddress", clinicAddress,
                "clinicPhone", clinicPhone);

        return thymeLeafService.buildApointmentMail(emailParams);
    }

    public LocalTime convertToLocalTime(String timeString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            Date date = sdf.parse(timeString);
            return date.toInstant().atZone(POLICY_ZONE).toLocalTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public LocalDate convertToLocalDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            Date date = sdf.parse(dateString);
            return date.toInstant().atZone(POLICY_ZONE).toLocalDate();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


}
