package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.ttn.e_commerce_project.constants.UserConstants.EMAIL_SUBJECT;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailServiceImpl  implements EmailService {

    final JavaMailSender javaMailSender;
    @Value("${admin.default.email}")
    String adminEmail;

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
    public void sendAcknowledgementMail(String email, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        if(email==null)
            email="animesh.yadav@tothenew.com";
        message.setTo(email);
        message.setSubject(EMAIL_SUBJECT);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Async
    public void sendProductInactiveMail(String email, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

}
