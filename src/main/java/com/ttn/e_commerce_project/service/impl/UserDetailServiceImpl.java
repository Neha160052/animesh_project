package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.entity.user.CustomUserDetails;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws ResourceNotFoundException {

        User user = userRepository.findByEmail(email).get();
        return new CustomUserDetails(user);
    }
}
