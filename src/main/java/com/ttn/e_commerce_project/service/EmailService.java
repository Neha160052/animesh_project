package com.ttn.e_commerce_project.service;

public interface EmailService {

    void sendLinkWithSubjectEmail(String email, String activationLink, String subject);
    void sendAcknowledgementMail(String email, String subject);
}
