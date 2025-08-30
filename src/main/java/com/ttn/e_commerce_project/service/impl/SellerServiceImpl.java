package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.AddressCo;
import com.ttn.e_commerce_project.dto.co.SellerCo;
import com.ttn.e_commerce_project.dto.co.SellerProfileCo;
import com.ttn.e_commerce_project.dto.co.UpdatePasswordCo;
import com.ttn.e_commerce_project.dto.vo.SellerProfileVo;
import com.ttn.e_commerce_project.entity.address.Address;
import com.ttn.e_commerce_project.entity.user.Customer;
import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.entity.user.Seller;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.exceptionhandling.PasswordMismatchException;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.AddressRepository;
import com.ttn.e_commerce_project.respository.SellerRepository;
import com.ttn.e_commerce_project.respository.UserRepository;
import com.ttn.e_commerce_project.service.EmailService;
import com.ttn.e_commerce_project.service.SellerService;
import com.ttn.e_commerce_project.util.ImageStorageUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SellerServiceImpl implements SellerService {

    UserRepository userRepository;
    SellerRepository sellerRepository;
    PasswordEncoder passwordEncoder;
    UserCommonService commonService;
    EmailService emailService;
    AddressRepository addressRepository;
    ImageStorageUtil imageStorageUtil;


    @Override
    public void register(SellerCo sellerCo) {

        if (userRepository.existsByEmail(sellerCo.getEmail())) {
            throw new InvalidArgumentException("Email already in use");
        }
        if (!sellerCo.getPassword().equals(sellerCo.getConfirmPassword())) {
            throw new InvalidArgumentException("Passwords do not match");
        }

        if (sellerRepository.existsByGst(sellerCo.getGst())) {
            throw new InvalidArgumentException("GST already exists provide unique one");
        }

        if (sellerRepository.existsByCompanyNameIgnoreCase(sellerCo.getCompanyName())) {
            throw new InvalidArgumentException("Company name already exists provide a unique name.");
        }
        User user = new User();
        Role sellerRole = commonService.findRoleByAuthority(RoleAuthority.SELLER);
        user.setEmail(sellerCo.getEmail());
        user.setPassword(passwordEncoder.encode(sellerCo.getPassword()));
        user.setFirstName(sellerCo.getFirstName());
        user.setMiddleName(sellerCo.getMiddleName());
        user.setLastName(sellerCo.getLastName());
        user.setRole(Set.of(sellerRole));
        user.setAddress(List.of(mapToAddress(sellerCo.getCompanyAddress())));
        userRepository.save(user);
        saveSeller(sellerCo,user);
    }

    public void saveSeller(SellerCo sellerCo,User user)
    {
        Seller seller = new Seller();
        seller.setUser(user);
        seller.setCompanyContact(sellerCo.getCompanyContact());
        seller.setCompanyName(sellerCo.getCompanyName());
        seller.setGst(sellerCo.getGst());
        sellerRepository.save(seller);
    }

    private Address mapToAddress(AddressCo addressCo) {
        Address address = new Address();
        address.setCity(addressCo.getCity());
        address.setState(addressCo.getState());
        address.setCountry(addressCo.getCountry());
        address.setAddressLine(addressCo.getAddressLine());
        address.setZipCode(addressCo.getZipCode());
        address.setLabel(addressCo.getLabel());
        return address;
    }

    @Override
    public SellerProfileVo getSellerProfile(String email) {
           Seller seller = commonService.findSellerByEmail(email);
           User user = seller.getUser();
           Address address = user.getAddress().getFirst();
           String imagePath = imageStorageUtil.buildProfileImageUrl("seller", user.getId());
           return new SellerProfileVo(
                   user.getId(),
                   user.getFirstName(),
                   user.getLastName(),
                   user.isActive(),
                   seller.getCompanyContact(),
                   seller.getCompanyName(),
                   imagePath,
                   seller.getGst(),
                   address.getCity(),
                   address.getState(),
                   address.getCountry(),
                   address.getAddressLine(),
                   address.getZipCode()
           );
    }
    @Transactional
    public void updateMyProfile(String email, SellerProfileCo sellerProfileCo)
    {
        Seller seller = commonService.findSellerByEmail(email);
        User user = seller.getUser();

        // these null checks are necessary because the data can be present or even not present due to patch type of request

        if (sellerProfileCo.getFirstName()!= null) user.setFirstName(sellerProfileCo.getFirstName().trim());
        if (sellerProfileCo.getLastName()!= null) user.setLastName(sellerProfileCo.getLastName().trim());

        if (sellerProfileCo.getCompanyContact() != null) seller.setCompanyContact(sellerProfileCo.getCompanyContact().trim());
        if (sellerProfileCo.getCompanyName() != null) seller.setCompanyName(sellerProfileCo.getCompanyName().trim());
        if (sellerProfileCo.getImage() != null) seller.setImage(sellerProfileCo.getImage().trim());
        if (sellerProfileCo.getGst() != null)
        {
            if (sellerRepository.existsByGst(sellerProfileCo.getGst())) {
                throw new InvalidArgumentException("GST already exists provide unique one");
            }
            seller.setGst(sellerProfileCo.getGst());
        }

        sellerRepository.save(seller);
    }

    @Transactional
    @Override
    public void updatePassword(String username, UpdatePasswordCo updatePasswordCo) {

        {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

            if (!passwordEncoder.matches(updatePasswordCo.getCurrentPassword(), user.getPassword())) {
                throw new InvalidArgumentException("Current password is incorrect");
            }

            if (!updatePasswordCo.getNewPassword().equals(updatePasswordCo.getConfirmPassword())) {
                throw new PasswordMismatchException("New password and Confirm password should match");
            }

            user.setPassword(passwordEncoder.encode(updatePasswordCo.getNewPassword()));
            userRepository.save(user);

            emailService.sendAcknowledgementMail(username,"Dear Seller your password has been updated successfully");
        }
    }

    @Transactional
    @Override
    public void updateAddress(String username, Long id, AddressCo addressCo)
    {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        Address address = addressRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found for this user"));

        // update fields
        address.setAddressLine(addressCo.getAddressLine());
        address.setCity(addressCo.getCity());
        address.setState(addressCo.getState());
        address.setCountry(addressCo.getCountry());
        address.setZipCode(addressCo.getZipCode());
        addressRepository.save(address);
    }

    @Override
    public void checkOwnership(Long id, String email) {
        Seller seller = commonService.findSellerByEmail(email);
        // Compare DB id with requested id
        if (!(seller.getUserid()==id)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
    }
}
