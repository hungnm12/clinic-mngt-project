package com.example.staffmngt.service;

import com.example.staffmngt.dto.req.AddRecordReq;
import com.example.staffmngt.dto.req.SearchRecReq;
import com.example.staffmngt.dto.res.GeneralResponse;

public interface RecordService {
    GeneralResponse addRecord(AddRecordReq addRecordReq);

    GeneralResponse getListRec(SearchRecReq searchRecReq);
}
