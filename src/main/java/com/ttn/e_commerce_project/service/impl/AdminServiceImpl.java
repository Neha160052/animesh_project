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
import com.ttn.e_commerce_project.service.CustomerService;
import com.ttn.e_commerce_project.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
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
            throw new ResourceNotFoundException("Customer with the given id does not exist" + id);
        else {
            updated = customerRepository.activateCustomerIfNotActive(id);
            if (updated == 1) {
                String email = userCommonService.findUserEmailById(id);
                Customer customer = userCommonService.findCustomerByEmail(email);
                customer.getUser().setPasswordUpdateDate(ZonedDateTime.now());
                customerRepository.save(customer);
                emailService.sendAcknowledgementMail(email,"Dear Customer your account has been activated by the admin now you can login");
            }
            return (updated == 1);
        }
    }

        public boolean activateSeller (Long id)
        {
            int updated;
            if (!sellerRepository.existsById(id))
                throw new ResourceNotFoundException("Seller with the given id does not exist" + id);
            else {
                updated = sellerRepository.activateSellerIfNotActive(id);
                System.out.println(updated);
                if (updated == 1)
                { String email = userCommonService.findUserEmailById(id);
                  Seller seller = sellerRepository.findByUserEmail(email).get();
                  seller.getUser().setPasswordUpdateDate(ZonedDateTime.now());
                  sellerRepository.save(seller);
                  emailService.sendAcknowledgementMail(email,"Dear seller you account has be activated by the admin now you can login");}
                return (updated == 1);
            }
        }
    public boolean deactivateCustomer(Long id) {
        int updated;
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer with the given id does not exist: " + id);
        } else {
            updated = customerRepository.deactivateCustomerIfActive(id);
            if (updated == 1) {
                return true; // Successfully deactivated
            }
            return false; // Already deactivated
        }
    }

    public boolean deactivateSeller(Long id) {
        int updated;
        if (!sellerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Seller with the given id does not exist: " + id);
        } else {
            updated = sellerRepository.deactivateSellerIfActive(id);
            if (updated == 1) {
                return true; // Successfully deactivated
            }
            return false; // Already deactivated
        }
    }

    // Phase 4

    public CategoryMetaDataField addMetaDataField(MetadataFieldCo metadataFieldCo){

        categoryMetadataRepo.findByNameIgnoreCase(metadataFieldCo.getName()).ifPresent(field-> {throw new InvalidArgumentException(FIELD_NAME_ALREADY_EXISTS);});

        CategoryMetaDataField field = new CategoryMetaDataField();
        field.setName(metadataFieldCo.getName());
        return categoryMetadataRepo.save(field);
    }

    public Page<MetadataFieldVo> getAllMetadataFields(int offset, int max, String sortBy, String order, String query)
    {
        Sort sort = order.equalsIgnoreCase("DESC")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(offset,max,sort);
        Page<CategoryMetaDataField> result;
        if(query!=null && !query.trim().isEmpty())
            result = categoryMetadataRepo.findByNameContainingIgnoreCase(query, pageable);
        else
            result = categoryMetadataRepo.findAll(pageable);
        return result.map(field -> new MetadataFieldVo(field.getId(), field.getName()));
    }
}
