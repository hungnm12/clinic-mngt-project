package com.example.staffmngt.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchReqDto implements Serializable {
    @JsonProperty("page")
    private Integer page;

    @Max(value = 100, message = "Tối đa 100")
    @JsonProperty("size")
    private Integer size = 10;

    @JsonProperty("sort_by")
    private String sortBy;

    @JsonProperty("sort_type")
    private String sortType = "asc";
}
