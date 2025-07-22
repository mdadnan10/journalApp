package com.adnantech.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void testMail() {
        emailService.sendEmail("10.adnanmd@gmail.com", "Testing Mail sender", "You are my Testing buddy");
    }
}
