package com.example.multitenancyservice.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SqlCredentialReq {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("url")
    private String url;

}
