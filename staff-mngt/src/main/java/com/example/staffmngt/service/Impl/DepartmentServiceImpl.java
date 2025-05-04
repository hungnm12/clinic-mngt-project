package com.example.staffmngt.service.Impl;

import com.example.staffmngt.dto.res.GeneralResponse;
import com.example.staffmngt.entity.DepartmentEntity;
import com.example.staffmngt.repository.DepartmentRepository;
import com.example.staffmngt.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    @Override
    public GeneralResponse addDepartment(String department) {
        DepartmentEntity departmentEntity = departmentRepository.findByName(department);
        if (departmentEntity != null) {
            return new GeneralResponse(HttpStatus.SC_CONFLICT, "", "department already exists", null);
        }

        DepartmentEntity newDepartment = new DepartmentEntity();
        newDepartment.setName(department);
        departmentRepository.save(newDepartment);

        return new GeneralResponse(HttpStatus.SC_CREATED, "", "department added", newDepartment);
    }
}
