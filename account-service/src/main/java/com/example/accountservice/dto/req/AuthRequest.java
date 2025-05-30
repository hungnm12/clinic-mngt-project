package com.example.accountservice.dto.req;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
