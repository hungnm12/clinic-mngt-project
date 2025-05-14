package com.example.staffmngt.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListStaffSearchReq extends SearchReqDto {

    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("role")
    private String role;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("specialty")
    private String specialty;

    @JsonProperty("email")
    private String email;

    @JsonProperty("department")
    private String department;
    @JsonProperty("staff_code")
    private String staffCode;

}
