package com.example.staffmngt.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookedPatientDto {
    @JsonProperty("patientName")
    private String patientName;

    @JsonProperty("patientTelephone")
    private String patientTelephone;

    @JsonProperty("patientEmail")
    private String patientEmail;
}
