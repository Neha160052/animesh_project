package com.ttn.e_commerce_project.helper;

import com.ttn.e_commerce_project.entity.VerificationToken;
import org.springframework.stereotype.Component;

@Component
public class ActivationHelper {


    public String sendActivationLink(VerificationToken token)
    {
        return "http://localhost:8080/activate?token=" + token.getToken();
    }
}

