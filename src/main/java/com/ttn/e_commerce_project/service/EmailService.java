package com.ttn.e_commerce_project.service;

public interface EmailService {

    void sendJavaActivationEmail(String email,String activationLink);
    void sendResetPasswordEmail(String email,String activationLink);
    void sendAcknowlegmentMail(Long id,String email);
}
