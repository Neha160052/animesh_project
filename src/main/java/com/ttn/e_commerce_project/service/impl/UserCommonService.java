package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.entity.token.VerificationToken;
import com.ttn.e_commerce_project.entity.user.Customer;
import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.entity.user.Seller;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.*;
import com.ttn.e_commerce_project.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    SellerRepository sellerRepository;
    CustomerRepository customerRepository;
    TokenServiceImpl verificationTokenService;
    EmailService emailService;
    TokenRepository tokenRepository;


    public Role findRoleByAuthority(RoleAuthority authority) {
        return roleRepository.findByAuthority(authority)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No role found for authority : %s", authority)));
    }

    public User findUserByEmail(String email) {
        User user =  userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No account found with email: " + email));

        log.info(user.getEmail());
        return user;
    }

    public Customer findCustomerByEmail(String email)
    {
       return customerRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    public Seller findSellerByEmail(String email)
    {
         return sellerRepository.findByUserEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
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
        emailService.sendLinkWithSubjectEmail(email, activationLink(token),"Please click on the link below to activate your account");
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

