package com.example.schedulerservice.service;

import com.example.schedulerservice.dto.req.AddSchedulerReq;
import com.example.schedulerservice.dto.res.GeneralResponse;
import com.example.schedulerservice.entity.SchedulerEntity;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface SchedulerService {

    GeneralResponse addScheduler(AddSchedulerReq addSchedulerReq) throws ParseException, ExecutionException, InterruptedException, TimeoutException;


}
