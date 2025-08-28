package com.ttn.e_commerce_project.service;

import com.ttn.e_commerce_project.service.impl.EmailServiceImpl;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)
class EmailServiceTest {

    @Autowired
    EmailServiceImpl emailService;

    @Test
    void testSendMail()
    {
        emailService.sendLinkWithSubjectEmail(
                "iamanimesh02@gmail.com","this is a test activation link : jsadfgjagfeffggf.fhfairkwe","this is a test case to check mail service"
        );
    }

}
