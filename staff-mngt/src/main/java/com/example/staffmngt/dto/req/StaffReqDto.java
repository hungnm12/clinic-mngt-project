package com.example.staffmngt.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class StaffReqDto {
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("role")
    private String role;

    @JsonProperty("email")
    private String email;

    @JsonProperty("department")
    private String department;

}
