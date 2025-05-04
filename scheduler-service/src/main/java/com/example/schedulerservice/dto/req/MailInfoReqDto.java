package com.example.schedulerservice.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailInfoReqDto {

    @JsonProperty("email_receiver")
    String emailReceiver;

    @JsonProperty("subject")
    String subject;

    @ToString.Exclude
    @JsonProperty("content_mail")
    Object contentMail;

    @JsonProperty("attach_file")
    Object attachFile;

}

