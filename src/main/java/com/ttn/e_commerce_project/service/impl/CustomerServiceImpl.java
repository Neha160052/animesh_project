package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.CustomerCo;
import com.ttn.e_commerce_project.entity.token.VerificationToken;
import com.ttn.e_commerce_project.entity.user.Customer;
import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.exceptionhandling.PasswordMismatchException;
import com.ttn.e_commerce_project.respository.CustomerRepository;
import com.ttn.e_commerce_project.respository.UserRepository;
import com.ttn.e_commerce_project.service.CustomerService;
import com.ttn.e_commerce_project.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    UserCommonService commonService;
    TokenServiceImpl verificationTokenService;
    EmailService emailService;

    @Override
    public void register(CustomerCo customerCo) {

        if (userRepository.existsByEmail(customerCo.getEmail())) {
            throw new InvalidArgumentException("Email already in use");
        }
        if (!customerCo.getPassword().equals(customerCo.getConfirmPassword())) {
            throw new PasswordMismatchException("Password and Confirm Password should match");
        }
        User user = new User();
        Role customerRole = commonService.findRoleByAuthority(RoleAuthority.CUSTOMER);
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
        emailService.sendLinkWithSubjectEmail(user.getEmail(), commonService.activationLink(token),"To activate your account click on the link below:");
    }

    @Override
    public CustomerProfileVo getMyProfile(String email) {

        Customer customer = customerRepository.findByUserEmail(email).orElseThrow(()-> new ResourceNotFoundException("Customer not found"));
        User user = customer.getUser();

        return new CustomerProfileVo(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.isActive(),
                customer.getContact(),
                customer.getImage()
        );
    }

    public AddressVo getMyAddress(String email,Long id){

        Customer customer = customerRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

       Address address = addressRepository.findByIdAndUserId(id, customer.getUserid())
               .orElseThrow(() -> new RuntimeException("Address not found for this customer"));
        return new AddressVo(
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getAddressLine(),
                address.getZipCode(),
                address.getLabel()
        );
    }

    @Override
    public String updateMyProfile(String email, CustomerProfileCo customerProfileCo) {

        Customer customer = customerRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        User user = customer.getUser();

        if (customerProfileCo.getFirstName() != null) {
            user.setFirstName(customerProfileCo.getFirstName());
        }
        if (customerProfileCo.getLastName() != null) {
            user.setLastName(customerProfileCo.getLastName());
        }
        if (customerProfileCo.getContact() != null) {
            customer.setContact(customerProfileCo.getContact());
        }
        if (customerProfileCo.getImage() != null) {
            customer.setImage(customerProfileCo.getImage());
        }

        customerRepository.save(customer);
        return "Profile updated successfully";
    }

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

            emailService.sendAcknowledgementMail(username,"Dear Customer your password has been updated successfully");
        }
    }

    }
