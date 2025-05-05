package com.example.staffmngt.service;

import com.example.staffmngt.dto.res.GeneralResponse;
import com.example.staffmngt.dto.req.AddShiftReq;
import com.example.staffmngt.entity.ShiftScheduleEntity;

public interface ShiftScheduleService {


    GeneralResponse getAllShifts();

    GeneralResponse createShift(AddShiftReq addShiftReq);

    GeneralResponse updateShift(AddShiftReq shiftSchedule);

    GeneralResponse deleteShift(String shiftCode);

}
