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
@AllArgsConstructor
@NoArgsConstructor
public class StaffResDto {

    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("role")
    private String role;

    @JsonProperty("email")
    private String email;

    @JsonProperty("department")
    private DepartmentEntity department;

    @JsonProperty("staff_code")
    private String staffCode;

    @JsonProperty("password")
    private String password;

    @JsonProperty("status")
    private String status;

    @JsonProperty("lstShift")
    private List<ShiftScheduleEntity> lstShift;
}
