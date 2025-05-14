package com.example.staffmngt.dto.res;

import com.example.staffmngt.entity.DepartmentEntity;
import com.example.staffmngt.entity.ShiftScheduleEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffResDto {

    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("age")
    private Integer age;
    @JsonProperty("role")
    private String role;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("specialty")
    private String specialty;
    @JsonProperty("email")
    private String email;
    @JsonProperty("staffCode")
    private String staffCode;
    @JsonProperty("password")
    private String password;
    @JsonProperty("status")
    private String status;
//    @JsonProperty("lstShift")
//    private Object lstShift;
}
