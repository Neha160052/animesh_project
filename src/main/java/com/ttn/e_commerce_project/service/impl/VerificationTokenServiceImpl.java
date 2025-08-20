package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.entity.VerificationToken;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.respository.TokenRepository;
import com.ttn.e_commerce_project.service.VerificationTokenService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    TokenRepository tokenRepository;

    @Override
    public VerificationToken createToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verifyToken = new VerificationToken(token,user,2);
        return tokenRepository.save(verifyToken);
    }

    @Override
    public Optional<VerificationToken> validateToken(String token) {
        return tokenRepository.findByToken(token).filter(t->!t.isExpire());
    }
}
