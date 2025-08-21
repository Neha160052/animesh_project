package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserCommonService {

    RoleRepository roleRepository;

    public Role findRoleByAuthority(RoleAuthority authority) {
        return roleRepository.findByAuthority(authority)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No role found for authority : %s", authority)));
    }
}
