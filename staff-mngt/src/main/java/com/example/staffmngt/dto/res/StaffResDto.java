package com.example.staffmngt.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class StaffResDto {

    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("age")
    private int age;

    @JsonProperty("role")
    private String role;

    @JsonProperty("email")
    private String email;

    @JsonProperty("department")
    private String department;

    @JsonProperty("staff_code")
    private String staffCode;
}
