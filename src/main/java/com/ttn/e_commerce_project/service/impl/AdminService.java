package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.vo.AddressVo;
import com.ttn.e_commerce_project.dto.vo.CustomerVo;
import com.ttn.e_commerce_project.dto.vo.SellerFlatVo;
import com.ttn.e_commerce_project.dto.vo.SellerVo;
import com.ttn.e_commerce_project.entity.user.Customer;
import com.ttn.e_commerce_project.entity.user.Seller;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.CustomerRepository;
import com.ttn.e_commerce_project.respository.SellerRepository;
import com.ttn.e_commerce_project.respository.UserRepository;
import com.ttn.e_commerce_project.service.CustomerService;
import com.ttn.e_commerce_project.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminService {

    CustomerRepository customerRepository;
    SellerRepository sellerRepository;
    EmailService emailService;
    UserCommonService userCommonService;

    public Page<CustomerVo> listAllCustomers(int pageSize, int pageOffset, String sort, String email)
    {
        Pageable pageable = PageRequest.of(pageOffset,pageSize, Sort.by(sort));
        return customerRepository.findAllCustomers(email,pageable);
    }

    public Page<SellerVo> listAllSellers(int pageSize, int pageOffset, String sort, String email) {

        Pageable pageable = PageRequest.of(pageOffset,pageSize, Sort.by(sort));
        Page<SellerFlatVo> sellerPage = sellerRepository.findAllSellers(email,pageable);

        return sellerPage.map(flat -> {
            AddressVo addressVo = new AddressVo(
                    flat.city(),
                    flat.state(),
                    flat.country(),
                    flat.addressLine(),
                    flat.zipCode(),
                    flat.label()
            );

            return new SellerVo(
                    flat.id(),
                    flat.fullName(),
                    flat.email(),
                    flat.isActive(),
                    flat.companyName(),
                    flat.companyContact(),
                    addressVo
            );
        });
    }

    public boolean activeCustomer(Long id) {
        int updated;
        if (!customerRepository.existsById(id))
            throw new ResourceNotFoundException("User with the given user id does not exist" + id);
        else {
            updated = customerRepository.activateCustomerIfNotActive(id);
            if (updated == 1) {
                String email = userCommonService.findUserEmailById(id);
                emailService.sendAcknowlegmentMail(id, email);
            }
            return (updated == 1);
        }
    }

        public boolean activateSeller (Long id)
        {
            int updated;
            if (!sellerRepository.existsById(id))
                throw new ResourceNotFoundException("User with the given user id does not exist" + id);
            else {
                updated = sellerRepository.activateSellerIfNotActive(id);
                if (updated == 1)
                { String email = userCommonService.findUserEmailById(id);
                  emailService.sendAcknowlegmentMail(id, email);}
                return (updated == 1);
            }
        }
    }
