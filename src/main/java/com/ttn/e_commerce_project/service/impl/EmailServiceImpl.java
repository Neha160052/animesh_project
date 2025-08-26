package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class EmailServiceImpl  implements EmailService {

    JavaMailSender javaMailSender;

    @Async
    public void sendLinkWithSubjectEmail(String toEmail, String activationLink, String subject)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(activationLink);
        javaMailSender.send(message);
    }

    @Async
    public void sendAcknowledgementMail(String email, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        javaMailSender.send(message);
    }


}
