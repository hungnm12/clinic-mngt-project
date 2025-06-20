package com.example.staffmngt.service.Impl;

import com.example.staffmngt.dto.req.AddRecordReq;
import com.example.staffmngt.dto.req.SearchRecReq;
import com.example.staffmngt.dto.req.SendReportMailReq;
import com.example.staffmngt.dto.res.GeneralResponse;
import com.example.staffmngt.dto.res.ListContentPageDto;
import com.example.staffmngt.dto.res.RecordResDto;
import com.example.staffmngt.dto.res.StaffResDto;
import com.example.staffmngt.entity.DepartmentEntity;
import com.example.staffmngt.entity.RecordEntity;
import com.example.staffmngt.entity.StaffEntity;
import com.example.staffmngt.kafka.service.KafkaProducerService;
import com.example.staffmngt.repository.DepartmentRepository;
import com.example.staffmngt.repository.RecordRepository;
import com.example.staffmngt.repository.StaffEntityRepository;
import com.example.staffmngt.service.RecordService;
import com.example.staffmngt.utils.JsonUtils;
import com.example.staffmngt.utils.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final StaffEntityRepository staffEntityRepository;
    private final DepartmentRepository departmentRepository;
    private final KafkaProducerService kafkaProducerService;

    public RecordServiceImpl(RecordRepository recordRepository, StaffEntityRepository staffEntityRepository, DepartmentRepository departmentRepository, KafkaProducerService kafkaProducerService) {
        this.recordRepository = recordRepository;
        this.staffEntityRepository = staffEntityRepository;
        this.departmentRepository = departmentRepository;
        this.kafkaProducerService = kafkaProducerService;
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


        SendReportMailReq s = new SendReportMailReq();
        s.setAssign(addRecordReq.getAssign());
        s.setDepartment(dpm.getName());
        s.setAssumption(addRecordReq.getAssumption());
        s.setPatientName(addRecordReq.getPatientName());
        s.setStaffName(addRecordReq.getStaffName());
        s.setNote(addRecordReq.getNote());
        s.setDiagnose(addRecordReq.getDiagnose());
        s.setPatientEmail(addRecordReq.getPatientEmail());
        s.setServiceType(addRecordReq.getServiceType());
        s.setSymptom(addRecordReq.getSymptom());
        s.setPatientDob(addRecordReq.getPatientDob());
        s.setPatientPhone(addRecordReq.getPatientPhone());
        s.setSubject("");
        String m = JsonUtils.marshalJsonAsPrettyString(s);

        try {
            kafkaProducerService.sendReport(m);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        return new GeneralResponse(HttpStatus.CREATED.value(), "", "", recordEntity);
    }

    @Override
    public GeneralResponse getListRec(SearchRecReq searchRecReq) {
        Sort sort;
        if (!StringUtil.isNullOrEmpty(searchRecReq.getSortBy())) {
            if (!StringUtil.isNullOrEmpty(searchRecReq.getSortType()) && searchRecReq.getSortType().equalsIgnoreCase("asc")) {
                sort = Sort.by(Sort.Direction.ASC, searchRecReq.getSortBy());
            } else {
                sort = Sort.by(Sort.Direction.DESC, searchRecReq.getSortBy());
            }
        } else {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }

        int page = 0;
        if (searchRecReq.getPage() != null) {
            page = Math.max(0, searchRecReq.getPage() - 1);
        }

        int size = 10;
        if (searchRecReq.getSize() != null) {
            size = searchRecReq.getSize() < 0 ? 5 : searchRecReq.getSize();
        }


        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RecordResDto> recordResDtos = recordRepository.getListRrc(searchRecReq.getStaffCode(), searchRecReq.getStaffName(), pageable);
        return new GeneralResponse(org.apache.http.HttpStatus.SC_OK, "", "service list", new ListContentPageDto<>(recordResDtos, recordResDtos.getContent()));
    }
}
