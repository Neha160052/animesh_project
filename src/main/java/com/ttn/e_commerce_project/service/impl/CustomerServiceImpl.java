package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.CustomerCo;
import com.ttn.e_commerce_project.entity.user.Customer;
import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.respository.CustomerRepository;
import com.ttn.e_commerce_project.respository.RoleRepository;
import com.ttn.e_commerce_project.respository.UserRepository;
import com.ttn.e_commerce_project.service.CustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerServiceImpl implements CustomerService {

     final UserRepository userRepository;
     final CustomerRepository customerRepository;
     final RoleRepository roleRepository;
     final PasswordEncoder passwordEncoder;


    @Override
    public ResponseEntity<String> register(CustomerCo customerCo) {

        if (userRepository.existsByEmail(customerCo.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (!customerCo.getPassword().equals(customerCo.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        User user = new User();
        Set<Role> customerRole = roleRepository.findByAuthority("Role_Customer");
        user.setEmail(customerCo.getEmail());
        user.setPassword(passwordEncoder.encode(customerCo.getPassword()));
        user.setFirstName(customerCo.getFirstName());
        user.setMiddleName(customerCo.getMiddleName());
        user.setLastName(customerCo.getLastName());
        user.setRole(customerRole);
        userRepository.save(user);
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setContact(Long.parseLong(customerCo.getPhoneNumber()));
        customerRepository.save(customer);
        return ResponseEntity.ok("Customer registered successfully");
    }
}
