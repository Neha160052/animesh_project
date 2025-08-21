package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.entity.user.CustomUserDetails;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailServiceImpl implements UserDetailsService {


    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws ResourceNotFoundException {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No account found with email: "));
        return new CustomUserDetails(user);
    }
}
