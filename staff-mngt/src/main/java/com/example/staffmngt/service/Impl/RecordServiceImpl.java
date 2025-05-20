package com.example.staffmngt.service.Impl;

import com.example.staffmngt.dto.req.AddRecordReq;
import com.example.staffmngt.dto.res.GeneralResponse;
import com.example.staffmngt.entity.DepartmentEntity;
import com.example.staffmngt.entity.RecordEntity;
import com.example.staffmngt.entity.StaffEntity;
import com.example.staffmngt.repository.DepartmentRepository;
import com.example.staffmngt.repository.RecordRepository;
import com.example.staffmngt.repository.StaffEntityRepository;
import com.example.staffmngt.service.RecordService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final StaffEntityRepository staffEntityRepository;
    private final DepartmentRepository departmentRepository;

    public RecordServiceImpl(RecordRepository recordRepository, StaffEntityRepository staffEntityRepository, DepartmentRepository departmentRepository) {
        this.recordRepository = recordRepository;
        this.staffEntityRepository = staffEntityRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public GeneralResponse addRecord(AddRecordReq addRecordReq) {
        if (addRecordReq.getPatientName() == null || addRecordReq.getPatientName().isEmpty()) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "", null);
        }
        StaffEntity staffEntity = staffEntityRepository.findByFullName(addRecordReq.getStaffName());
        if (staffEntity == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "", null);
        }
        DepartmentEntity dpm = departmentRepository.findByName(addRecordReq.getDepartment());
        if (dpm == null) {
            return new GeneralResponse(HttpStatus.NO_CONTENT.value(), "", "", null);
        }
        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setAssign(addRecordReq.getAssign());
        recordEntity.setDepartment(dpm);
        recordEntity.setAssumption(addRecordReq.getAssumption());
        recordEntity.setPatientName(addRecordReq.getPatientName());
        recordEntity.setStaffName(addRecordReq.getStaffName());
        recordEntity.setNote(addRecordReq.getNote());
        recordEntity.setDiagnose(addRecordReq.getDiagnose());
        recordEntity.setPatientEmail(addRecordReq.getPatientEmail());
        recordEntity.setServiceType(addRecordReq.getServiceType());
        recordEntity.setSymptom(addRecordReq.getSymptom());
        recordEntity.setPatientDob(addRecordReq.getPatientDob());
        recordEntity.setStaffCode(staffEntity.getStaffCode());
        recordEntity.setPatientPhone(addRecordReq.getPatientPhone());
        recordEntity.setPatientName(addRecordReq.getPatientName());
        recordRepository.save(recordEntity);


        return new GeneralResponse(HttpStatus.CREATED.value(), "", "", recordEntity);
    }
}
