package com.example.staffmngt.service;

import com.example.staffmngt.dto.req.AddServiceReq;
import com.example.staffmngt.dto.res.GeneralResponse;

public interface ServiceClinicService {

    GeneralResponse addService(AddServiceReq addServiceReq);

    GeneralResponse deleteService(String serviceName);

    GeneralResponse updateService(AddServiceReq addServiceReq);

    GeneralResponse getListServices(AddServiceReq addServiceReq);

}
