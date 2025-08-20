package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.CustomerCo;
import com.ttn.e_commerce_project.entity.VerificationToken;
import com.ttn.e_commerce_project.entity.user.Customer;
import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import com.ttn.e_commerce_project.exceptionhandling.EmailNotFoundException;
import com.ttn.e_commerce_project.respository.CustomerRepository;
import com.ttn.e_commerce_project.respository.RoleRepository;
import com.ttn.e_commerce_project.respository.UserRepository;
import com.ttn.e_commerce_project.service.CustomerService;
import com.ttn.e_commerce_project.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CustomerServiceImpl implements CustomerService {

      UserRepository userRepository;
      CustomerRepository customerRepository;
      RoleRepository roleRepository;
      PasswordEncoder passwordEncoder;
      VerificationTokenServiceImpl verificationTokenService;
      EmailService emailService;


    @Override
    public void register(CustomerCo customerCo) {

        if (userRepository.existsByEmail(customerCo.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (!customerCo.getPassword().equals(customerCo.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        User user = new User();
        Role customerRole = roleRepository.findByAuthority(RoleAuthority.CUSTOMER).get();
        user.setEmail(customerCo.getEmail());
        user.setPassword(passwordEncoder.encode(customerCo.getPassword()));
        user.setFirstName(customerCo.getFirstName());
        user.setMiddleName(customerCo.getMiddleName());
        user.setLastName(customerCo.getLastName());
        user.setRole(Set.of(customerRole));
        userRepository.save(user);
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setContact(customerCo.getPhoneNumber());
        customerRepository.save(customer);
        VerificationToken token = verificationTokenService.createToken(user);
        emailService.sendJavaActivationEmail(user.getEmail(), activationLink(token));
    }


    public ResponseEntity<String> activateCustomer(String token) {
        return verificationTokenService.validateToken(token)
                .map(verificationToken -> {
                    User user = verificationToken.getUser();
                    user.setActive(true);
                    userRepository.save(user);
                    return ResponseEntity.ok("Account activated Successfully");
                }).orElse(ResponseEntity.badRequest().body("Invalid or expired token"));
    }

    public void resendActivationLink(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("No account found with email: " + email));

        VerificationToken token = verificationTokenService.createToken(user);
        emailService.sendJavaActivationEmail(email, activationLink(token));
    }

    public String activationLink(VerificationToken token) {
        return "http://localhost:8080/activate?token=" + token.getToken();
    }

}
