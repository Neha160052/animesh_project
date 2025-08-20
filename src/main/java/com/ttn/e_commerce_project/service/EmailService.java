package com.ttn.e_commerce_project.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailService {

    JavaMailSender javaMailSender;

    EmailService(JavaMailSender javaMailSender)
    {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendJavaActivationEmail(String toEmail,String activationLink )
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("To activate your account click on the link below: ");
        message.setText(activationLink);

        javaMailSender.send(message);
    }

}
