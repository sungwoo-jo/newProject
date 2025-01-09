package com.sw.newProject.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class MailDto {
    @Value("${spring.mail.username}")
    private String senderMail; // 발신자 이메일

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String enable;
}
