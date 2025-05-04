package com.example.schedulerservice.service.Impl;

import com.example.schedulerservice.service.ThymeLeafService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

public class ThymeLeafServiceImpl implements ThymeLeafService {


    private final TemplateEngine templateEngine;

    public ThymeLeafServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String buildApointmentMail(Map<String, Object> map) {
        Context context = new Context();
        context.setVariables(map);
        return templateEngine.process("cf-mail", context);
    }
}
