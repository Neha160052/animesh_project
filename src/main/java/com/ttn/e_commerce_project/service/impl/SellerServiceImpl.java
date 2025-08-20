package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.SellerCo;
import com.ttn.e_commerce_project.entity.address.Address;
import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.entity.user.Seller;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import com.ttn.e_commerce_project.respository.RoleRepository;
import com.ttn.e_commerce_project.respository.SellerRepository;
import com.ttn.e_commerce_project.respository.UserRepository;
import com.ttn.e_commerce_project.service.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SellerServiceImpl implements SellerService {

      UserRepository userRepository;
      SellerRepository sellerRepository;
      RoleRepository roleRepository;
      PasswordEncoder passwordEncoder;
      ModelMapper modelMapper;

    @Override
    public void register(SellerCo sellerCo) {

        if (userRepository.existsByEmail(sellerCo.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (!sellerCo.getPassword().equals(sellerCo.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        User user = new User();
        Role sellerRole = roleRepository.findByAuthority(RoleAuthority.SELLER).get();
        user.setEmail(sellerCo.getEmail());
        user.setPassword(passwordEncoder.encode(sellerCo.getPassword()));
        user.setFirstName(sellerCo.getFirstName());
        user.setMiddleName(sellerCo.getMiddleName());
        user.setLastName(sellerCo.getLastName());
        user.setRole(Set.of(sellerRole));
        userRepository.save(user);
        saveSeller(sellerCo,user);
    }

    public void saveSeller(SellerCo sellerCo,User user)
    {
        Seller seller = new Seller();
        seller.setUser(user);
        Address address = modelMapper.map(sellerCo.getCompanyAddress(), Address.class);
        seller.setCompanyContact(sellerCo.getCompanyContact());
        seller.setCompanyName(sellerCo.getCompanyName());
        seller.setGst(sellerCo.getGst());
        seller.setAddress(address);
        sellerRepository.save(seller);
    }
}
