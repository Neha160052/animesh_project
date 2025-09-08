package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.entity.product.Product;
import com.ttn.e_commerce_project.entity.token.VerificationToken;
import com.ttn.e_commerce_project.entity.user.Customer;
import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.entity.user.Seller;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import com.ttn.e_commerce_project.exceptionhandling.AccountLockedException;
import com.ttn.e_commerce_project.exceptionhandling.AccountNotActiveException;
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

import static com.ttn.e_commerce_project.constants.UserConstants.*;
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
    ProductRepository productRepo;

    public Role findRoleByAuthority(RoleAuthority authority) {
        return roleRepository.findByAuthority(authority)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ROLE_NOT_FOUND, authority)));
    }

    public User findUserByEmail(String email) {
        User user =  userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_BY_EMAIL + email));

        log.info(user.getEmail());
        return user;
    }

    public Customer findCustomerByEmail(String email)
    {
       return customerRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_NOT_FOUND));
    }

    public Seller findSellerByEmail(String email)
    {
         return sellerRepository.findByUserEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException(SELLER_NOT_FOUND));
    }
    public ResponseEntity<String> activateUser(String token) {
        return verificationTokenService.validateToken(token)
                .map(verificationToken -> {
                    User user = verificationToken.getUser();
                    user.setActive(true);
                    user.setPasswordUpdateDate(ZonedDateTime.now());
                    userRepository.save(user);
                    return ResponseEntity.ok(ACCOUNT_ACTIVATED);
                }).orElse(ResponseEntity.badRequest().body(INVALID_OR_EXPIRED_TOKEN));
    }

    public void resendActivationLink(String email) {
        User user = findUserByEmail(email);
        if (user.isActive()) {
            throw new InvalidArgumentException(USER_ALREADY_ACTIVE);
        }
        tokenRepository.deleteByUser(user);
        VerificationToken token = verificationTokenService.createToken(user);
        emailService.sendLinkWithSubjectEmail(email, activationLink(token),ACTIVATION_EMAIL_SUBJECT);
    }

    public String activationLink(VerificationToken token) {
        return ACTIVATION_LINK_BASE + token.getToken();
    }


    public String  findUserEmailById(Long id)
    {
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(USER_NOT_FOUND_BY_ID +id));
        user.setPasswordUpdateDate(ZonedDateTime.now());
        return user.getEmail();
    }

    public Product findProductById(Long id)
    {
        return productRepo.findById(id).orElseThrow(()->new ResourceNotFoundException(PRODUCT_NOT_FOUND));
    }

    public void verifyUser(String email)
    {
        User user = findUserByEmail(email);
        if(!user.isActive())
            throw  new AccountNotActiveException(ACCOUNT_NOT_ACTIVE);
        if(user.isLocked())
            throw new AccountLockedException(ACCOUNT_LOCKED);
    }
}

