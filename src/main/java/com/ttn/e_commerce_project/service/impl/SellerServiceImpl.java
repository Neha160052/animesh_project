package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.AddressCo;
import com.ttn.e_commerce_project.dto.co.SellerCo;
import com.ttn.e_commerce_project.entity.address.Address;
import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.entity.user.Seller;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import com.ttn.e_commerce_project.respository.SellerRepository;
import com.ttn.e_commerce_project.respository.UserRepository;
import com.ttn.e_commerce_project.service.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

    @Override
    public void register(SellerCo sellerCo) {

        if (userRepository.existsByEmail(sellerCo.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (!sellerCo.getPassword().equals(sellerCo.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (sellerRepository.existsByGst(sellerCo.getGst())) {
            throw new IllegalArgumentException("GST already exists provide unique one");
        }

        if (sellerRepository.existsByCompanyNameIgnoreCase(sellerCo.getCompanyName())) {
            throw new IllegalArgumentException("Company name already exists provide a unique name.");
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

           Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
           User user = seller.getUser();
           Address address = user.getAddress().getFirst();
           return new SellerProfileVo(
                   user.getId(),
                   user.getFirstName(),
                   user.getLastName(),
                   user.isActive(),
                   seller.getCompanyContact(),
                   seller.getCompanyName(),
                   seller.getImage(),
                   seller.getGst(),
                   address.getCity(),
                   address.getState(),
                   address.getCountry(),
                   address.getAddressLine(),
                   address.getZipCode()
           );
    }
    public void updateMyProfile(String email, SellerProfileCo req)
    {
        Seller seller = sellerRepository.findByUserEmail(email)
                                        .orElseThrow(()->new ResourceNotFoundException("seller not found"));
        User user = seller.getUser();
        List<Address> addresses = user.getAddress();

        Address address;
        if (addresses.isEmpty()) {    //To do : come back and read more how else this situation can be handled
            address = new Address();
            addresses.add(address);  // add first and only address
        } else {
            address = addresses.get(0);  // always take the first one
        }
        // these null checks are necessary because the data can be present or even not present due to patch type of request

        if (req.getFirstName()!= null) user.setFirstName(req.getFirstName().trim());
        if (req.getLastName()!= null) user.setLastName(req.getLastName().trim());

        if (req.getCompanyContact() != null) seller.setCompanyContact(req.getCompanyContact().trim());
        if (req.getCompanyName() != null) seller.setCompanyName(req.getCompanyName().trim());
        if (req.getImage() != null) seller.setImage(req.getImage().trim());
        if (req.getGst() != null)
        {
            if (sellerRepository.existsByGst(req.getGst())) {
                throw new InvalidArgumentException("GST already exists provide unique one");
            }
            seller.setGst(req.getGst());
        }

        if (req.getAddress() != null) {
            AddressCo a = req.getAddress();
            if (a.getCity() != null) address.setCity(a.getCity().trim());
            if (a.getState() != null) address.setState(a.getState().trim());
            if (a.getCountry() != null) address.setCountry(a.getCountry().trim());
            if (a.getAddressLine() != null) address.setAddressLine(a.getAddressLine().trim());
            if (a.getZipCode() != 0) address.setZipCode(a.getZipCode());
        }
        sellerRepository.save(seller);
    }

    @Override
    public void updatePassword(String username, UpdatePasswordCo updatePasswordCo) {

        {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

            if (!passwordEncoder.matches(updatePasswordCo.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }

            if (!updatePasswordCo.getNewPassword().equals(updatePasswordCo.getConfirmPassword())) {
                throw new IllegalArgumentException("New password and Confirm password should match");
            }

            user.setPassword(passwordEncoder.encode(updatePasswordCo.getNewPassword()));
            userRepository.save(user);

            emailService.sendAcknowledgementMail(username,"Dear Seller your password has been updated successfully");
        }
    }

    @Override
    public void updateAddress(String username, Long id, AddressCo addressCo)
    {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        Address address = user.getAddress().getFirst();
        // update fields
        address.setAddressLine(addressCo.getAddressLine());
        address.setCity(addressCo.getCity());
        address.setState(addressCo.getState());
        address.setCountry(addressCo.getCountry());
        address.setZipCode(addressCo.getZipCode());

        addressRepository.save(address);
    }
}
