package com.ttn.e_commerce_project.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)
class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    void testSendMail()
    {
        emailService.sendJavaActivationEmail(
                "iamanimesh02@gmail.com","this is a test activation link : jsadfgjagfeffggf.fhfairkwe"
        );
    }

}
