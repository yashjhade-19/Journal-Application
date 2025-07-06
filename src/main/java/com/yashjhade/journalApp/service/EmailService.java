package com.yashjhade.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);

            // ✅ Set sender address - must match spring.mail.username
            mail.setFrom("yashjhade19@gmail.com");

            javaMailSender.send(mail);
            log.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Exception while sending to {}: {}", to, e.getMessage(), e);
        }
    }
}
