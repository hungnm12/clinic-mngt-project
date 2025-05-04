package com.example.schedulerservice.service.Impl;

import com.example.schedulerservice.dto.req.AddSchedulerReq;
import com.example.schedulerservice.dto.res.GeneralResponse;
import com.example.schedulerservice.entity.SchedulerEntity;
import com.example.schedulerservice.repository.SchedulerRepository;
import com.example.schedulerservice.service.SchedulerService;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
public class AddSchedulerServiceImpl implements SchedulerService {

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
                .apmtDate(addSchedulerReq.getApmtDate())
                .apmtTime(addSchedulerReq.getApmtTime())
                .drName(addSchedulerReq.getDrName())
                .patientEmail(addSchedulerReq.getPatientEmail())
                .patientName(addSchedulerReq.getPatientName())
                .patientTelephone(addSchedulerReq.getPatientTelephone())
                .orderedSrv(addSchedulerReq.getOrderedSrv())
                .build();

        schedulerRepository.save(schedulerEntity);

        return new GeneralResponse(HttpStatus.SC_OK, "", "Order added successfully!", schedulerEntity);
    }


}
