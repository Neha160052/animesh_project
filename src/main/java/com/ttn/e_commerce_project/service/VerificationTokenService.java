package com.ttn.e_commerce_project.service;

import com.ttn.e_commerce_project.entity.VerificationToken;
import com.ttn.e_commerce_project.entity.user.User;

import java.util.Optional;

public interface VerificationTokenService {

    public VerificationToken createToken(User user);
    Optional<VerificationToken> validateToken(String token);
}
