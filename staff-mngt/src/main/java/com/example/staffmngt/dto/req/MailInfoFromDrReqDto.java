package com.example.staffmngt.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailInfoFromDrReqDto {

    @JsonProperty("email_receiver")
    String emailReceiver;

    @JsonProperty("subject")
    String subject;

    @JsonProperty("drName")
    String drName;

    @JsonProperty("appointmentDate")
    String appointmentDate;

    @JsonProperty("clinicPhone")
    String clinicPhone;

    @JsonProperty("clinicName")
    String clinicName;

}

