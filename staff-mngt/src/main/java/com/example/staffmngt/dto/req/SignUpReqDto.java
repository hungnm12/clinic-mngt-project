package com.example.staffmngt.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SignUpReqDto {

    @JsonProperty("email")
    private String email;

    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("password")
    private String password;
}
