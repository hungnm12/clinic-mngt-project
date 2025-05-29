package com.example.staffmngt.service.Impl;


import com.example.staffmngt.dto.req.AddSchedulerReq;
import com.example.staffmngt.dto.req.AddShiftReq;
import com.example.staffmngt.dto.res.BookedPatientDto;
import com.example.staffmngt.dto.res.ScheduleListResDto;
import com.example.staffmngt.entity.ShiftScheduleEntity;
import com.example.staffmngt.entity.StaffEntity;
import com.example.staffmngt.repository.ShiftRepository;
import com.example.staffmngt.repository.StaffEntityRepository;
import com.example.staffmngt.service.ShiftScheduleService;
import com.example.staffmngt.utils.JsonUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.staffmngt.dto.res.GeneralResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@Slf4j
public class ShiftScheduleServiceImpl implements ShiftScheduleService {
    private static final ZoneId POLICY_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private StaffEntityRepository staffEntityRepository;


    @Override
    public GeneralResponse getAllShifts(Long id) {
        if (id == null) {
            return new GeneralResponse(HttpStatus.SC_BAD_REQUEST, "", "staff code not found", null);
        }
        List<ScheduleListResDto> s = shiftRepository.getShiftsByStaffId(id);
        if (s == null || s.isEmpty()) {
            return new GeneralResponse(HttpStatus.SC_NO_CONTENT, "", "no shift found", null);
        }

        return new GeneralResponse(HttpStatus.SC_OK, "", "list fetched", s);
    }

    @Override
    public GeneralResponse createShift(AddShiftReq shiftSchedule) {

        log.info("SchedulerCode from input: '{}'", shiftSchedule.getSchedulerCode());
        StaffEntity staffEntity = staffEntityRepository.findByStaffCode(shiftSchedule.getStaffCode());

        if (staffEntity == null) {
            log.error("staff not found");
            return new GeneralResponse(HttpStatus.SC_BAD_REQUEST, "", "staff not found", null);
        }
        ShiftScheduleEntity shiftScheduleEntity = new ShiftScheduleEntity();
        shiftScheduleEntity.setShiftCode(shiftSchedule.getShiftCode());
        shiftScheduleEntity.setStaff(staffEntity);
        shiftScheduleEntity.setBookedPatient(shiftSchedule.getBookedPatient());
        shiftScheduleEntity.setBookedTime(shiftSchedule.getBookedTime());
        shiftScheduleEntity.setNote(shiftSchedule.getNote());
        shiftScheduleEntity.setStatus(shiftSchedule.getStatus());
        shiftScheduleEntity.setSchedulerCode(shiftSchedule.getSchedulerCode());

        shiftRepository.save(shiftScheduleEntity);
        return new GeneralResponse(HttpStatus.SC_CREATED, "", "shift created", null);
    }

    @Override
    public GeneralResponse updateShift(AddShiftReq shiftSchedule) {
        if (shiftSchedule.getShiftCode() == null || shiftSchedule.getShiftCode().isEmpty()) {
            return new GeneralResponse(HttpStatus.SC_BAD_REQUEST, "", "empty input", null);
        }
        StaffEntity staffEntity = staffEntityRepository.findByStaffCode(shiftSchedule.getStaffCode());
        if (staffEntity == null) {
            return new GeneralResponse(HttpStatus.SC_BAD_REQUEST, "", "staff not found", null);
        }
        ShiftScheduleEntity existingShiftSchedule = shiftRepository.findShiftScheduleBySchedulerCode(shiftSchedule.getSchedulerCode());
        if (existingShiftSchedule == null) {
            return new GeneralResponse(HttpStatus.SC_BAD_REQUEST, "", "shift not found", null);
        }

        existingShiftSchedule.setBookedTime(shiftSchedule.getBookedTime());

        existingShiftSchedule.setStaff(staffEntity);

        shiftRepository.save(existingShiftSchedule);
        return new GeneralResponse(HttpStatus.SC_OK, "", "shift updated", existingShiftSchedule);
    }

    @Override
    public GeneralResponse deleteShift(String schedulerCode) {

        if (schedulerCode == null || schedulerCode.isEmpty()) {
            return new GeneralResponse(HttpStatus.SC_BAD_REQUEST, "", "empty input", null);
        }
        ShiftScheduleEntity shiftScheduleEntity = shiftRepository.findShiftScheduleBySchedulerCode(schedulerCode);
        shiftRepository.delete(shiftScheduleEntity);
        return new GeneralResponse(HttpStatus.SC_OK, "", "delete", null);
    }

    public void processShiftSchedule(AddSchedulerReq addSchedulerReq) {
        log.info("processShiftSchedule: {}", addSchedulerReq);
        log.info("Input schedulerCode: '{}'", addSchedulerReq.getSchedulerCode());
        StaffEntity staffEntity = staffEntityRepository.findByFullName(addSchedulerReq.getDrName());
        if (staffEntity == null) {
            log.error("staff not found");
            return;
        }

        BookedPatientDto bookedPatientDto = new BookedPatientDto();
        bookedPatientDto.setPatientEmail(addSchedulerReq.getPatientEmail());
        bookedPatientDto.setPatientName(addSchedulerReq.getPatientName());
        bookedPatientDto.setPatientTelephone(addSchedulerReq.getPatientTelephone());
        String bookedPatient = JsonUtils.marshalJsonAsPrettyString(bookedPatientDto);
        AddShiftReq addShiftReq = new AddShiftReq();
        addShiftReq.setShiftCode(addSchedulerReq.getOrderedSrv());
        addShiftReq.setStaffCode(staffEntity.getStaffCode());
        addShiftReq.setBookedPatient(bookedPatient);
        addShiftReq.setBookedTime(convertToZonedDateTime(addSchedulerReq.getApmtDate().toString(), addSchedulerReq.getApmtTime().toString()));
        addShiftReq.setNote(addSchedulerReq.getNote() == null ? "" : addSchedulerReq.getNote());
        addShiftReq.setStatus(addSchedulerReq.getStatus() == null ? "" : addSchedulerReq.getStatus());
        addShiftReq.setSchedulerCode(addSchedulerReq.getSchedulerCode());
        createShift(addShiftReq);
    }

    private ZonedDateTime convertToZonedDateTime(String date, String time) {
        log.info("date: {}, time: {}", date, time);
        String dateTimeString = date + " " + time; // e.g., "2025-05-15 10:30:00"

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);

        // Set time zone to Asia/Bangkok (ICT)

        ZonedDateTime zonedDateTime = localDateTime.atZone(POLICY_ZONE);

        log.info("Parsed ZonedDateTime: {}", zonedDateTime);
        return zonedDateTime;
    }
}
