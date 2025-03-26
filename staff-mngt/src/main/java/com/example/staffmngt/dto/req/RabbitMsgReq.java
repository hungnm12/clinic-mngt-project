package com.example.staffmngt.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RabbitMsgReq {

    @JsonProperty("topic")
    private String topic;
    @JsonProperty("queue")
    private String queue;
    @JsonProperty("data")
    private Object data;
}
