package com.yashjhade.journalApp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Disabled
    @Test
    void testSendMail() {
        emailService.sendEmail(
                "yashjhade2510@gmail.com",           // ➊ recipient
                "Testing Java‑Mail‑Sender",          // ➋ subject
                "Hi, this is the King here side 👑"   // ➌ body
        );
    }
}
