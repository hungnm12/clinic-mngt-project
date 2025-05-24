package com.example.staffmngt.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdReqDto {
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("age")
    private Integer age;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("specialty")
    private String specialty;
    @JsonProperty("role")
    private String role;

    @JsonProperty("email")
    private String email;

    @JsonProperty("department")
    private String department;
    @JsonProperty("staffcode")
    private String staffCode;

}
