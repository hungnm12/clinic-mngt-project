package com.example.schedulerservice.service;

import com.example.schedulerservice.dto.req.MailInfoReqDto;

import java.util.List;

public interface MailService {

    void sendMail(MailInfoReqDto mailInfoReqDto);

    void sendMail(List<MailInfoReqDto> mailInfoReqs);
}
