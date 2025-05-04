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

    GeneralResponse addStaff(StaffReqDto staff);

    GeneralResponse updateStaff(UpdReqDto staff);

    GeneralResponse deleteStaff(String staffCode);
//
//    StaffEntity login(String email, String password);
//
//    StaffEntity signUp(SignUpReqDto signUp);

    GeneralResponse searchStaff(UpdReqDto updReqDto);

    GeneralResponse getListStaff(ListStaffSearchReq staffSearchReq);


}
