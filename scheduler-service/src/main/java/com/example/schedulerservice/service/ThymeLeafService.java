package com.example.schedulerservice.service;

import java.util.Map;

public interface ThymeLeafService {

    String buildApointmentMail(Map<String, Object> map);

    String buildDenyMail(Map<String, Object> map);

    String buildReportMail(Map<String, Object> map);
}
