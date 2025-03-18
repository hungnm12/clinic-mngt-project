package com.example.patientservice.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponse {
    private int code;
    private String errorMsg;
    private String message;
    private Object data;
}
