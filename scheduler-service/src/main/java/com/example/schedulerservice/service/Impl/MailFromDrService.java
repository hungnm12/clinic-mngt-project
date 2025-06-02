package com.example.schedulerservice.service.Impl;

import com.example.schedulerservice.dto.req.AddSchedulerReq;
import com.example.schedulerservice.dto.req.MailInfoFromDrReqDto;
import com.example.schedulerservice.dto.req.MailInfoReqDto;
import com.example.schedulerservice.dto.req.SendReportMailReq;
import com.example.schedulerservice.service.MailService;
import com.example.schedulerservice.service.ThymeLeafService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MailFromDrService {

    @Autowired
    MailService mailService;

    @Autowired
    ThymeLeafService thymeLeafService;

    public void sendDenyMail(MailInfoFromDrReqDto mailInfoFromDrReqDto) {
        String data = buildMailInfor(mailInfoFromDrReqDto);
        MailInfoReqDto mail = MailInfoReqDto.builder()
                .emailReceiver(mailInfoFromDrReqDto.getEmailReceiver())
                .subject("Appointment Denied")
                .contentMail(data)
                .attachFile(null)
                .build();

        mailService.sendMail(mail);
    }


    private String buildMailInfor(MailInfoFromDrReqDto mailInfoFromDrReqDto) {
        Map<String, Object> emailParams = Map.of(
                "clinicName", mailInfoFromDrReqDto.getClinicName(),
                "doctorName", mailInfoFromDrReqDto.getDrName(),
                "appointmentDate", mailInfoFromDrReqDto.getAppointmentDate(),
                "clinicPhone", mailInfoFromDrReqDto.getClinicPhone());

        return thymeLeafService.buildDenyMail(emailParams);
    }

    public void sendReportMail(SendReportMailReq mailInfoFromDrReqDto) {
        String data = buildReportMailInfor(mailInfoFromDrReqDto);
        MailInfoReqDto mail = MailInfoReqDto.builder()
                .emailReceiver(mailInfoFromDrReqDto.getPatientEmail())
                .subject("Medical Report")
                .contentMail(data)
                .attachFile(null)
                .build();
        mailService.sendMail(mail);
    }


    private String buildReportMailInfor(SendReportMailReq mailInfoFromDrReqDto) {
        Map<String, Object> emailParams = new HashMap<>();
        emailParams.put("patientName", mailInfoFromDrReqDto.getPatientName());
        emailParams.put("patientPhone", mailInfoFromDrReqDto.getPatientPhone());
        emailParams.put("patientDob", mailInfoFromDrReqDto.getPatientDob());
        emailParams.put("patientEmail", mailInfoFromDrReqDto.getPatientEmail());
        emailParams.put("staffName", mailInfoFromDrReqDto.getStaffName());
        emailParams.put("serviceType", mailInfoFromDrReqDto.getServiceType());
        emailParams.put("diagnose", mailInfoFromDrReqDto.getDiagnose());
        emailParams.put("symptom", mailInfoFromDrReqDto.getSymptom());
        emailParams.put("assumption", mailInfoFromDrReqDto.getAssumption());
        emailParams.put("assign", mailInfoFromDrReqDto.getAssign());
        emailParams.put("note", mailInfoFromDrReqDto.getNote());

        return thymeLeafService.buildReportMail(emailParams);
    }

}
