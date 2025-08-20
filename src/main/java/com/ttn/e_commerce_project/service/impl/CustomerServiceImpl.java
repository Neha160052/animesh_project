package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.CustomerCo;
import com.ttn.e_commerce_project.entity.VerificationToken;
import com.ttn.e_commerce_project.entity.user.Customer;
import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.enums.RoleAuthority;
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
    public ResponseEntity<String> register(CustomerCo customerCo) {

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
        String activationLink = "http://localhost:8080/activate?token=" + token.getToken();
        emailService.sendJavaActivationEmail(user.getEmail(),activationLink);
        return ResponseEntity.ok("Customer registered successfully");
    }

}
