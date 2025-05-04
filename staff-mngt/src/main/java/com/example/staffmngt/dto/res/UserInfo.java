package com.example.staffmngt.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("roles")
    private String roles;

    @JsonProperty("type")
    private String type;

    @JsonProperty("status")
    private String status;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("staffCode")
    private String staffCode;
}

