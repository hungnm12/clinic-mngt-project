package com.example.schedulerservice.service.Impl;

import com.example.schedulerservice.constant.NotificationConstant;
import com.example.schedulerservice.dto.req.MailInfoReqDto;
import com.example.schedulerservice.service.MailService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.example.schedulerservice.extra.exception.ServerErrorException;


@Service
@Slf4j
public class MailServiceImpl implements MailService {
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String email;

    @Value("${spring.mail.password}")
    private String password;

    private String emailRateLimiterKey = "EMAIL_RATE_LIMITER";

    @Value("${email.rate-limiter.max_requests}")
    private int emailRateLimiterMaxRequests;

    @Value("${email.rate-limiter.duration}")
    private int emailRateLimiterDuration;


    private Session getMailSession() {
        //
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.trust", host);
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
    }


    @Override
    public void sendMail(MailInfoReqDto mailInfoReqDto) {
        sendMail(getMailSession(), mailInfoReqDto);
    }

    @Override
    public void sendMail(List<MailInfoReqDto> mailInfoReqs) {

    }

    private void sendMail(Session session, MailInfoReqDto mailInfoReq) {
        log.info("[sendMail] - send_to:{}", mailInfoReq.getEmailReceiver());

        Message message = new MimeMessage(session);

        try {
            Multipart multipart = new MimeMultipart();
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(mailInfoReq.getContentMail(), CONTENT_TYPE_TEXT_HTML);
            multipart.addBodyPart(messageBodyPart);

            if (mailInfoReq.getAttachFile() != null) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(creareAttachFile(mailInfoReq.getAttachFile()));
                multipart.addBodyPart(attachmentPart);
            }

            String[] emailReceivers = mailInfoReq.getEmailReceiver().split(",");
            List<InternetAddress> internetAddressList = new ArrayList<>();

            // set list email receiver
            for (int i = 0; i < emailReceivers.length; i++) {
                if (emailReceivers[i].length() != 0 && !emailReceivers[i].equals("")) {
                    internetAddressList.add(new InternetAddress(emailReceivers[i]));
                }
            }
            InternetAddress[] internetAddresses = internetAddressList.toArray(new InternetAddress[internetAddressList.size()]);

            message.setRecipients(Message.RecipientType.TO, internetAddresses);
            message.setFrom(new InternetAddress(email));
            message.setSubject(mailInfoReq.getSubject() == null || mailInfoReq.getSubject().trim().isEmpty() ? "IDG - Thông báo" : mailInfoReq.getSubject());
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            e.getMessage();
            throw new ServerErrorException("Không thể gửi mail! Vui lòng liên hệ quản trị viên! {}");
        }

    }

    private File creareAttachFile(Object attachFile) {

        String fileName = "log.txt";
        File myObj = null;
        try {
            Path path = Path.of(fileName);
            if (Files.exists(path)) {
                Files.delete(path);
            }
            myObj = File.createTempFile(NotificationConstant.TEMP_LOG_PREFIX, NotificationConstant.TEMP_LOG_SUFFIX, Files.createTempDirectory(NotificationConstant.TEMP_EMAIL_PREFIX).toFile());
            try (FileWriter fw = new FileWriter(myObj)) {
                fw.write(attachFile.toString());
            }
            return myObj;
        } catch (IOException e) {
            log.error("[creareAttachFile] ERROR: ", e.getStackTrace(), e);
        }
        return null;
    }


//    @PostConstruct
//    public void init() {
//        log.info("sending mail...");
//        sendMail(getMailSession(), new MailInfoReqDto("vtdung220402@gmail.com", "test", "fadsfadssafdsafsad", null));
//    }
}
