package com.example.staffmngt.service.Impl;

import com.example.staffmngt.dto.req.AddServiceReq;
import com.example.staffmngt.dto.res.GeneralResponse;
import com.example.staffmngt.dto.res.ListContentPageDto;
import com.example.staffmngt.dto.res.ServiceResDto;
import com.example.staffmngt.entity.DepartmentEntity;
import com.example.staffmngt.entity.ServiceEntity;
import com.example.staffmngt.repository.DepartmentRepository;
import com.example.staffmngt.repository.ServiceRepository;
import com.example.staffmngt.service.ServiceClinicService;
import com.example.staffmngt.utils.StringUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;


@Service
@Slf4j
public class ServiceClinicServiceImpl implements ServiceClinicService {

    private final DepartmentRepository departmentRepository;
    private final ServiceRepository serviceRepository;

    public ServiceClinicServiceImpl(DepartmentRepository departmentRepository, ServiceRepository serviceRepository) {
        this.departmentRepository = departmentRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public GeneralResponse addService(AddServiceReq addServiceReq) {
        log.info("[add service] {}", addServiceReq);

        if (addServiceReq == null) {
            return new GeneralResponse(HttpStatus.SC_BAD_REQUEST, "", "input not found", null);
        }


        DepartmentEntity department = departmentRepository.findByName(addServiceReq.getDepartment());

        if (department == null) {
            return new GeneralResponse(HttpStatus.SC_CONFLICT, "", "department is not found", null);
        }

        if (serviceRepository.findByServiceCode(addServiceReq.getServiceCode()) != null) {
            return new GeneralResponse(HttpStatus.SC_CONFLICT, "", "service already exists", null);
        }
        if (serviceRepository.findByServiceName(addServiceReq.getServiceName()) != null) {
            return new GeneralResponse(HttpStatus.SC_CONFLICT, "", "service already exists", null);
        }


        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setServiceCode(addServiceReq.getServiceCode());
        serviceEntity.setServiceName(addServiceReq.getServiceName());
        serviceEntity.setNote(addServiceReq.getNote());
        serviceEntity.setDepartment(department);
        serviceEntity.setPrice(addServiceReq.getPrice());
        serviceRepository.save(serviceEntity);

        return new GeneralResponse(HttpStatus.SC_CREATED, "", "Service added", serviceEntity);
    }

    @Override
    public GeneralResponse deleteService(String serviceName) {
        if (serviceName == null) {
            return new GeneralResponse(HttpStatus.SC_BAD_REQUEST, "", "serviceName not found", null);
        }
        ServiceEntity s = serviceRepository.findByServiceName(serviceName);
        serviceRepository.delete(s);

        return new GeneralResponse(HttpStatus.SC_NO_CONTENT, "", "Service deleted", null);
    }

    @Override
    public GeneralResponse updateService(AddServiceReq addServiceReq) {
        log.info("[updateService] {}", addServiceReq);
        if (addServiceReq == null) {
            return new GeneralResponse(HttpStatus.SC_BAD_REQUEST, "", "input not found", null);
        }

        ServiceEntity existingService = serviceRepository.findByServiceName(addServiceReq.getServiceName());
        if (existingService == null) {
            return new GeneralResponse(HttpStatus.SC_NOT_FOUND, "", "service not found", null);
        }

        DepartmentEntity department = departmentRepository.findByName(addServiceReq.getDepartment());
        if (department == null) {
            return new GeneralResponse(HttpStatus.SC_CONFLICT, "", "department is not found", null);
        }

        // Check if service code belongs to a different service
        ServiceEntity byCode = serviceRepository.findByServiceCode(addServiceReq.getServiceCode());
        if (byCode != null && !byCode.getId().equals(existingService.getId())) {
            return new GeneralResponse(HttpStatus.SC_CONFLICT, "", "another service with the same code already exists", null);
        }

        // Check if service name belongs to a different service (optional since we're updating by name)
        ServiceEntity byName = serviceRepository.findByServiceName(addServiceReq.getServiceName());
        if (byName != null && !byName.getId().equals(existingService.getId())) {
            return new GeneralResponse(HttpStatus.SC_CONFLICT, "", "another service with the same name already exists", null);
        }

        // Perform the update
        existingService.setServiceName(addServiceReq.getServiceName());
        existingService.setServiceCode(addServiceReq.getServiceCode());
        existingService.setPrice(addServiceReq.getPrice());
        existingService.setNote(addServiceReq.getNote());
        existingService.setDepartment(department);

        serviceRepository.save(existingService);

        return new GeneralResponse(HttpStatus.SC_OK, "", "service updated successfully", existingService);
    }


    @Override
    public GeneralResponse getListServices(AddServiceReq addServiceReq) {

        Sort sort;
        if (!StringUtil.isNullOrEmpty(addServiceReq.getSortBy())) {
            if (!StringUtil.isNullOrEmpty(addServiceReq.getSortType()) && addServiceReq.getSortType().equalsIgnoreCase("asc")) {
                sort = Sort.by(Sort.Direction.ASC, addServiceReq.getSortBy());
            } else {
                sort = Sort.by(Sort.Direction.DESC, addServiceReq.getSortBy());
            }
        } else {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }

        int page = 0;
        if (addServiceReq.getPage() != null) {
            page = Math.max(0, addServiceReq.getPage() - 1);
        }

        int size = 10;
        if (addServiceReq.getSize() != null) {
            size = addServiceReq.getSize() < 0 ? 5 : addServiceReq.getSize();
        }


        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ServiceResDto> ServiceResDtoPage = serviceRepository.getListOfServices(addServiceReq.getServiceCode(), addServiceReq.getServiceName(),addServiceReq.getNote(), pageable);
        return new GeneralResponse(HttpStatus.SC_OK, "", "service list", new ListContentPageDto<>(ServiceResDtoPage, ServiceResDtoPage.getContent()));

    }

    @Override
    public GeneralResponse getAllServices() {
        return new GeneralResponse(HttpStatus.SC_OK, "", "service list", serviceRepository.findAll());
    }
//    @PostConstruct
//    private void init() {
//        String s = Base64.getDecoder().decode("$10$3q466kZpXhxU6HuSKIPjZOBZb.RnDNbN2LiIZ7uqFqg3PeNMU.eBu")
//    }
}
