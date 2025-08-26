package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.entity.token.VerificationToken;
import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.RoleRepository;
import com.ttn.e_commerce_project.respository.TokenRepository;
import com.ttn.e_commerce_project.respository.UserRepository;
import com.ttn.e_commerce_project.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE,makeFinal = true)
public class UserCommonService {

    RoleRepository roleRepository;
    UserRepository userRepository;
    TokenServiceImpl verificationTokenService;
    EmailService emailService;
    TokenRepository tokenRepository;


    public Role findRoleByAuthority(RoleAuthority authority) {
        return roleRepository.findByAuthority(authority)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No role found for authority : %s", authority)));
    }

    public User findUserByEmail(String email)
    {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No account found with email: " + email));
    }

    public ResponseEntity<String> activateUser(String token) {
        return verificationTokenService.validateToken(token)
                .map(verificationToken -> {
                    User user = verificationToken.getUser();
                    user.setActive(true);
                    user.setPasswordUpdateDate(ZonedDateTime.now());
                    userRepository.save(user);
                    return ResponseEntity.ok("Account activated Successfully");
                }).orElse(ResponseEntity.badRequest().body("Invalid or expired token"));
    }

    public void resendActivationLink(String email) {
        User user = findUserByEmail(email);
        if (user.isActive()) {
            throw new InvalidArgumentException("User is already active. Activation link cannot be resent.");
        }
        tokenRepository.deleteByUser(user);
        VerificationToken token = verificationTokenService.createToken(user);
        emailService.sendJavaActivationEmail(email, activationLink(token));
    }

    public String activationLink(VerificationToken token) {
        return "http://localhost:8080/activate?token=" + token.getToken();
    }


    public String  findUserEmailById(Long id)
    {
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User with user id not found:"+id));
        user.setPasswordUpdateDate(ZonedDateTime.now());
        return user.getEmail();
    }
}

