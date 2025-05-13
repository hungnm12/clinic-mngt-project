package com.example.schedulerservice.service.Impl;

import com.example.schedulerservice.dto.req.AddSchedulerReq;
import com.example.schedulerservice.dto.res.GeneralResponse;
import com.example.schedulerservice.entity.SchedulerEntity;
import com.example.schedulerservice.repository.SchedulerRepository;
import com.example.schedulerservice.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class AddSchedulerServiceImpl implements SchedulerService {
    private static final ZoneId POLICY_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final SchedulerRepository schedulerRepository;
    private final ThymeLeafServiceImpl thymeLeafService;

    public AddSchedulerServiceImpl(SchedulerRepository schedulerRepository, ThymeLeafServiceImpl thymeLeafService) {
        this.schedulerRepository = schedulerRepository;
        this.thymeLeafService = thymeLeafService;
    }

    @Override
    public GeneralResponse addScheduler(AddSchedulerReq addSchedulerReq) {

        String orderCode = "Order_" + "_" + addSchedulerReq.getOrderedSrv() + new Random().nextInt(100);
        if (schedulerRepository.existsBySchedulerCode(orderCode)) {
            return new GeneralResponse(HttpStatus.SC_CONFLICT, "", "Order cdode already exists!", null);
        }

        SchedulerEntity schedulerEntity = SchedulerEntity.builder()
                .schedulerCode(orderCode)
                .note(addSchedulerReq.getNote())
                .dateApmt(convertToLocalDate(addSchedulerReq.getApmtDate().toString(), addSchedulerReq.getApmtTime().toString()))
                .drName(addSchedulerReq.getDrName())
                .patientEmail(addSchedulerReq.getPatientEmail())
                .patientName(addSchedulerReq.getPatientName())
                .patientTelephone(addSchedulerReq.getPatientTelephone())
                .orderedSrv(addSchedulerReq.getOrderedSrv())
                .build();

        schedulerRepository.save(schedulerEntity);

        return new GeneralResponse(HttpStatus.SC_OK, "", "Order added successfully!", schedulerEntity);
    }

    private Date convertToLocalDate(String date, String time) {
        log.info("date: {}, time: {}", date, time);
        String dateTimeString = date + "T" + time; // e.g., "2025-05-15T17:30:00"
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        ZonedDateTime zonedDateTime = localDateTime.atZone(POLICY_ZONE);
        return Date.from(zonedDateTime.toInstant());
    }


}
