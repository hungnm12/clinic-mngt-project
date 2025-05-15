package com.example.staffmngt.controller;


import com.example.staffmngt.configuration.TenantContext;
import com.example.staffmngt.dto.req.AddRecordReq;
import com.example.staffmngt.dto.res.GeneralResponse;
import com.example.staffmngt.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @PostMapping("/add")
    public GeneralResponse addRecord(@RequestBody AddRecordReq addRecordReq, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setTenant(tenantId);
        return recordService.addRecord(addRecordReq);
    }
}
