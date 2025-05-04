package com.example.schedulerservice.service;

import com.example.schedulerservice.dto.req.AddSchedulerReq;
import com.example.schedulerservice.dto.res.GeneralResponse;
import com.example.schedulerservice.entity.SchedulerEntity;

public interface SchedulerService {

    GeneralResponse addScheduler(AddSchedulerReq addSchedulerReq);


}
