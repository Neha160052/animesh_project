package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.AddressCo;
import com.ttn.e_commerce_project.dto.co.CustomerCo;
import com.ttn.e_commerce_project.dto.co.CustomerProfileCo;
import com.ttn.e_commerce_project.dto.co.UpdatePasswordCo;
import com.ttn.e_commerce_project.dto.vo.AddressVo;
import com.ttn.e_commerce_project.dto.vo.CustomerProfileVo;
import com.ttn.e_commerce_project.entity.address.Address;
import com.ttn.e_commerce_project.entity.token.VerificationToken;
import com.ttn.e_commerce_project.entity.user.Customer;
import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.exceptionhandling.PasswordMismatchException;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.AddressRepository;
import com.ttn.e_commerce_project.respository.CustomerRepository;
import com.ttn.e_commerce_project.respository.UserRepository;
import com.ttn.e_commerce_project.service.CustomerService;
import com.ttn.e_commerce_project.service.EmailService;
import com.ttn.e_commerce_project.util.ImageStorageUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    UserCommonService commonService;
    TokenServiceImpl verificationTokenService;
    EmailService emailService;
    AddressRepository addressRepository;
    ImageStorageUtil imageStorageUtil;

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

        Customer customer = commonService.findCustomerByEmail(email);
        User user = customer.getUser();
        String imagePath = imageStorageUtil.buildProfileImageUrl("customer", user.getId());
        return new CustomerProfileVo(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.isActive(),
                customer.getContact(),
                imagePath
        );
    }

    public List<AddressVo> getMyAddresses(String email){

        Customer customer = commonService.findCustomerByEmail(email);
        List<Address> addresses = addressRepository.findByUserId(customer.getUser().getId());

        return addresses.stream()
                .map(address -> new AddressVo(
                        address.getCity(),
                        address.getState(),
                        address.getCountry(),
                        address.getAddressLine(),
                        address.getZipCode(),
                        address.getLabel()
                ))
                .toList();
    }

    @Transactional
    @Override
    public String updateMyProfile(String email, CustomerProfileCo customerProfileCo) {
        log.info("inside the update my profile service");
        Customer customer = commonService.findCustomerByEmail(email);
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

        customerRepository.save(customer);
        log.info("persisted the customer in the db");
        return "Profile updated successfully";
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

            emailService.sendAcknowledgementMail(username,"Dear Customer your password has been updated successfully");
        }
    }

    @Transactional
    public String addCustomerAddress(String email, AddressCo addressCo)
    {
        Customer customer = commonService.findCustomerByEmail(email);
        Address address = new Address();
        address.setCity(addressCo.getCity());
        address.setState(addressCo.getState());
        address.setCountry(addressCo.getCountry());
        address.setAddressLine(addressCo.getAddressLine());
        address.setLabel(addressCo.getLabel());
        address.setZipCode(addressCo.getZipCode());
        customer.getUser().getAddress().add(address);
        userRepository.save(customer.getUser());
        return "Address saved successfully for userId: " + customer.getUser().getId();
    }

    @Transactional
    @Override
    public String deleteAddress(String email ,Long id) {
        Customer customer = commonService.findCustomerByEmail(email);

        Address address = addressRepository.findByIdAndUserId(id, customer.getUserid())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        addressRepository.delete(address);
        return "Address deleted successfully";
    }

    @Transactional
    @Override
    public String updateAddress(Long id, AddressCo addressCo) {

        Address address = addressRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("address not found"));

        address.setAddressLine(addressCo.getAddressLine());
        address.setCity(addressCo.getCity());
        address.setState(addressCo.getState());
        address.setCountry(addressCo.getCountry());
        address.setZipCode(addressCo.getZipCode());
        address.setLabel(addressCo.getLabel());
        Address savedAddress = addressRepository.save(address);
        if(savedAddress.getId() == id)
            return "Address updated successfully";
        else
            return "Address could not be updated";
    }

    @Override
    public void checkOwnership(Long id, String email) {

        Customer customer = commonService.findCustomerByEmail(email);
        // Compare DB id with requested id
        if (!(customer.getUserid()==id)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
    }
}
