package com.example.staffmngt.service;

import com.example.staffmngt.dto.req.ListStaffSearchReq;
import com.example.staffmngt.dto.req.SignUpReqDto;
import com.example.staffmngt.dto.req.StaffReqDto;
import com.example.staffmngt.dto.req.UpdReqDto;
import com.example.staffmngt.dto.res.GeneralResponse;
import com.example.staffmngt.dto.res.StaffResDto;
import com.example.staffmngt.entity.StaffEntity;

import java.util.List;


public interface StaffService {

    GeneralResponse addStaff(StaffReqDto staff,String tenantId);

    GeneralResponse updateStaff(UpdReqDto staff);

    GeneralResponse deleteStaff(String staffCode);
//
//    StaffEntity login(String email, String password);
//
//    StaffEntity signUp(SignUpReqDto signUp);

    GeneralResponse searchStaff(UpdReqDto updReqDto);

    GeneralResponse getListStaff(ListStaffSearchReq staffSearchReq);

    GeneralResponse getAllStaff();

    GeneralResponse getStaff(String staffCode);

    GeneralResponse getDrBySpecialty(String specialty);

    GeneralResponse getSpecialtyByDr(String staffCode);

    GeneralResponse isStaffExist(String drName);

    GeneralResponse isSpecialtyExist(String specialty);


}
