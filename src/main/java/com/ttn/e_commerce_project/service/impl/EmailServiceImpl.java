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
    public void sendJavaActivationEmail(String toEmail,String activationLink)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("To activate your account click on the link below: ");
        message.setText(activationLink);
        javaMailSender.send(message);
    }

    @Async
    public void sendResetPasswordEmail(String email,String activationLink )
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Click on the link below to reset you password: ");
        message.setText(activationLink);
        javaMailSender.send(message);
    }

    @Async
    public void sendAcknowlegmentMail(Long id,String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your account has been activated by the admin now you can login");
        javaMailSender.send(message);
    }


}
