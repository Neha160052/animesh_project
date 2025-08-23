package com.ttn.e_commerce_project.service;

import com.ttn.e_commerce_project.entity.token.VerificationToken;
import com.ttn.e_commerce_project.entity.user.User;

import java.util.Optional;

public interface TokenService {

    VerificationToken createToken(User user);
    Optional<VerificationToken> validateToken(String token);
}
