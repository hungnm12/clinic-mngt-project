package com.example.schedulerservice.service.Impl;

import com.example.schedulerservice.dto.req.AddSchedulerReq;
import com.example.schedulerservice.dto.res.GeneralResponse;
import com.example.schedulerservice.entity.SchedulerEntity;
import com.example.schedulerservice.kafka.service.KafkaProducerService;
import com.example.schedulerservice.repository.SchedulerRepository;
import com.example.schedulerservice.service.SchedulerService;
import com.example.schedulerservice.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class AddSchedulerServiceImpl implements SchedulerService {
    private static final ZoneId POLICY_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final SchedulerRepository schedulerRepository;
    private final ThymeLeafServiceImpl thymeLeafService;
    private final KafkaProducerService kafkaProducerService;

    public AddSchedulerServiceImpl(SchedulerRepository schedulerRepository, ThymeLeafServiceImpl thymeLeafService, KafkaProducerService kafkaProducerService) {
        this.schedulerRepository = schedulerRepository;
        this.thymeLeafService = thymeLeafService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public GeneralResponse addScheduler(AddSchedulerReq addSchedulerReq) throws ParseException, ExecutionException, InterruptedException, TimeoutException {

        String orderCode = "Order_" + "_" + addSchedulerReq.getOrderedSrv() + new Random().nextInt(100);
        if (schedulerRepository.existsBySchedulerCode(orderCode)) {
            return new GeneralResponse(HttpStatus.SC_CONFLICT, "", "Order cdode already exists!", null);
        }

        SchedulerEntity schedulerEntity = SchedulerEntity.builder()
                .schedulerCode(orderCode)
                .note(addSchedulerReq.getNote())
                .dateApmt(convertToZonedDateTime(addSchedulerReq.getApmtDate().toString(), addSchedulerReq.getApmtTime().toString()))
                .drName(addSchedulerReq.getDrName())
                .patientEmail(addSchedulerReq.getPatientEmail())
                .patientName(addSchedulerReq.getPatientName())
                .patientTelephone(addSchedulerReq.getPatientTelephone())
                .orderedSrv(addSchedulerReq.getOrderedSrv())
                .build();

        String msg = JsonUtils.marshalJsonAsPrettyString(addSchedulerReq);
        kafkaProducerService.publishScheduler(msg);
        schedulerRepository.save(schedulerEntity);


        return new GeneralResponse(HttpStatus.SC_OK, "", "Order added successfully!", schedulerEntity);
    }

    private ZonedDateTime convertToZonedDateTime(String date, String time) {
        log.info("date: {}, time: {}", date, time);
        String dateTimeString = date + " " + time; // e.g., "2025-05-15 10:30:00"

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);

        // Set time zone to Asia/Bangkok (ICT)

        ZonedDateTime zonedDateTime = localDateTime.atZone(POLICY_ZONE);

        log.info("Parsed ZonedDateTime: {}", zonedDateTime);
        return zonedDateTime;
    }


}
