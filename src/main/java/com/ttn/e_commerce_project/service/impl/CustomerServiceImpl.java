package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.AddressCo;
import com.ttn.e_commerce_project.dto.co.CustomerCo;
import com.ttn.e_commerce_project.dto.co.CustomerProfileCo;
import com.ttn.e_commerce_project.dto.co.UpdatePasswordCo;
import com.ttn.e_commerce_project.dto.vo.AddressVo;
import com.ttn.e_commerce_project.dto.vo.CustomerProfileVo;
import com.ttn.e_commerce_project.entity.address.Address;
import com.ttn.e_commerce_project.entity.category.Category;
import com.ttn.e_commerce_project.entity.token.VerificationToken;
import com.ttn.e_commerce_project.entity.user.Customer;
import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.exceptionhandling.PasswordMismatchException;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.AddressRepository;
import com.ttn.e_commerce_project.respository.CategoryRepository;
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

import java.util.List;
import java.util.Set;

import static com.ttn.e_commerce_project.constants.UserConstants.*;

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
            throw new InvalidArgumentException(EMAIL_ALREADY_IN_USE);
        }
        if (!customerCo.getPassword().equals(customerCo.getConfirmPassword())) {
            throw new PasswordMismatchException(PASSWORD_MISMATCH);
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
        emailService.sendLinkWithSubjectEmail(user.getEmail(), commonService.activationLink(token),ACTIVATION_EMAIL_SUBJECT);
    }

    @Override
    public CustomerProfileVo getMyProfile(String email) {

        Customer customer = commonService.findCustomerByEmail(email);
        User user = customer.getUser();
        String imagePath=null;
        if (imageStorageUtil.profileImageExists(CUSTOMER_USER_TYPE, user.getId())) {
            imagePath = imageStorageUtil.buildProfileImageUrl(CUSTOMER_USER_TYPE, user.getId());
        }
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
        return PROFILE_UPDATED_SUCCESSFULLY;
    }

    @Transactional
    @Override
    public void updatePassword(String username, UpdatePasswordCo updatePasswordCo) {
        {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_NOT_FOUND));

            if (!passwordEncoder.matches(updatePasswordCo.getCurrentPassword(), user.getPassword())) {
                throw new InvalidArgumentException(CURRENT_PASSWORD_INCORRECT);
            }

            if (!updatePasswordCo.getNewPassword().equals(updatePasswordCo.getConfirmPassword())) {
                throw new PasswordMismatchException(NEW_PASSWORD_MISMATCH);
            }
            user.setPassword(passwordEncoder.encode(updatePasswordCo.getNewPassword()));
            userRepository.save(user);

            emailService.sendAcknowledgementMail(username,PASSWORD_UPDATED_SUCCESSFULLY );
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
        return ADDRESS_SAVED_SUCCESSFULLY + customer.getUser().getId();
    }

    @Transactional
    @Override
    public String deleteAddress(String email ,Long id) {
        Customer customer = commonService.findCustomerByEmail(email);

        Address address = addressRepository.findByIdAndUserId(id, customer.getUserid())
                .orElseThrow(() -> new ResourceNotFoundException(ADDRESS_NOT_FOUND));

        addressRepository.delete(address);
        return ADDRESS_DELETED_SUCCESSFULLY;
    }

    @Transactional
    @Override
    public String updateAddress(Long id, AddressCo addressCo) {

        Address address = addressRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(ADDRESS_NOT_FOUND));

        address.setAddressLine(addressCo.getAddressLine());
        address.setCity(addressCo.getCity());
        address.setState(addressCo.getState());
        address.setCountry(addressCo.getCountry());
        address.setZipCode(addressCo.getZipCode());
        address.setLabel(addressCo.getLabel());
        Address savedAddress = addressRepository.save(address);
        if(savedAddress.getId() == id)
            return ADDRESS_UPDATED_SUCCESSFULLY;
        else
            return ADDRESS_COULD_NOT_BE_UPDATED;
    }

    @Override
    public void checkOwnership(Long id, String email) {

        Customer customer = commonService.findCustomerByEmail(email);
        // Compare DB id with requested id
        if (!(customer.getUserid()==id)) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }
    }
}
