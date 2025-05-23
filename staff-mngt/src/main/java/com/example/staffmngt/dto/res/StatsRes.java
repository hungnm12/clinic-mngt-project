package com.example.staffmngt.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsRes {
    @JsonProperty("count")
    private int count;
    @JsonProperty("timestamp")
    private String timestamp;
}
