package com.example.staffmngt.service.Impl;


import com.example.staffmngt.dto.req.AddShiftReq;
import com.example.staffmngt.entity.ShiftScheduleEntity;
import com.example.staffmngt.entity.StaffEntity;
import com.example.staffmngt.repository.ShiftRepository;
import com.example.staffmngt.repository.StaffEntityRepository;
import com.example.staffmngt.service.ShiftScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.staffmngt.dto.res.GeneralResponse;


@Service
@Slf4j
public class ShiftScheduleServiceImpl implements ShiftScheduleService {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private StaffEntityRepository staffEntityRepository;


    @Override
    public GeneralResponse getAllShifts() {
        return new GeneralResponse(HttpStatus.SC_OK, "", "list fetched", shiftRepository.findAll());
    }

    @Override
    public GeneralResponse createShift(AddShiftReq shiftSchedule) {
        if (shiftSchedule.getShiftCode() == null || shiftSchedule.getShiftCode().isEmpty()) {
            return new GeneralResponse(HttpStatus.SC_BAD_REQUEST, "", "empty input", null);
        }

        StaffEntity staffEntity = staffEntityRepository.findByStaffCode(shiftSchedule.getStaffCode());

        if (staffEntity == null) {
            return new GeneralResponse(HttpStatus.SC_BAD_REQUEST, "", "staff not found", null);
        }
        ShiftScheduleEntity shiftScheduleEntity = new ShiftScheduleEntity();
        shiftScheduleEntity.setShiftCode(shiftSchedule.getShiftCode());
        shiftScheduleEntity.setStaff(staffEntity);
        shiftScheduleEntity.setShiftEnd(shiftSchedule.getShiftEnd());
        shiftScheduleEntity.setShiftStart(shiftSchedule.getShiftStart());
        shiftScheduleEntity.setDayOfWeek(shiftSchedule.getDayOfWeek());
        shiftScheduleEntity.setBookedTime(shiftSchedule.getBookedTime());
        shiftRepository.save(shiftScheduleEntity);


        return null;
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
        ShiftScheduleEntity existingShiftSchedule = shiftRepository.findShiftScheduleByShiftCode(shiftSchedule.getShiftCode());
        if (existingShiftSchedule == null) {
            return new GeneralResponse(HttpStatus.SC_BAD_REQUEST, "", "shift not found", null);
        }
        existingShiftSchedule.setShiftEnd(shiftSchedule.getShiftEnd());
        existingShiftSchedule.setShiftStart(shiftSchedule.getShiftStart());
        existingShiftSchedule.setDayOfWeek(shiftSchedule.getDayOfWeek());
        existingShiftSchedule.setBookedTime(shiftSchedule.getBookedTime());
        existingShiftSchedule.setStaff(staffEntity);

        shiftRepository.save(existingShiftSchedule);
        return new GeneralResponse(HttpStatus.SC_OK, "", "shift updated", existingShiftSchedule);
    }

    @Override
    public GeneralResponse deleteShift(String shiftCode) {

        if (shiftCode == null || shiftCode.isEmpty()) {
            return new GeneralResponse(HttpStatus.SC_BAD_REQUEST, "", "empty input", null);
        }
        ShiftScheduleEntity shiftScheduleEntity = shiftRepository.findShiftScheduleByShiftCode(shiftCode);
        shiftRepository.delete(shiftScheduleEntity);
        return new GeneralResponse(HttpStatus.SC_OK, "", "delete", null);
    }
}
