package com.example.accountservice.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMsgRes {


    @JsonProperty("data")
    private Object data;

    @JsonProperty("tenantId")
    private String tenantId;
}
