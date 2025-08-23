package com.ttn.e_commerce_project.bootstrap;

import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.RoleRepository;
import com.ttn.e_commerce_project.respository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataInitializer implements CommandLineRunner {

    final RoleRepository roleRepository;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    @Value("${admin.default.password}")
    String password;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        Arrays.stream(RoleAuthority.values()).forEach(authority-> roleRepository.findByAuthority(authority).orElseGet(() -> {
            Role role = new Role();
            role.setAuthority(authority);
            return roleRepository.save(role);
        }));

        Role adminRole = roleRepository.findByAuthority(RoleAuthority.ADMIN).orElseThrow(()-> new ResourceNotFoundException("Role not found"));
        if(!userRepository.existsByEmail("admin@ecommerce.com"))
        {
            User admin = new User();
            admin.setEmail("admin@ecommerce.com");
            admin.setPassword(passwordEncoder.encode(password));
            admin.setFirstName("admin");
            admin.setRole(Set.of(adminRole));
            admin.setActive(true);
            userRepository.save(admin);
        }
    }
}
