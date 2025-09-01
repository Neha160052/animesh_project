package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.CategoryCo;
import com.ttn.e_commerce_project.dto.co.MetadataFieldCo;
import com.ttn.e_commerce_project.dto.vo.*;
import com.ttn.e_commerce_project.entity.category.Category;
import com.ttn.e_commerce_project.entity.category.CategoryMetaDataField;
import com.ttn.e_commerce_project.entity.user.Customer;
import com.ttn.e_commerce_project.entity.user.Seller;
import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.CategoryMetadataFieldRepository;
import com.ttn.e_commerce_project.respository.CategoryRepository;
import com.ttn.e_commerce_project.respository.CustomerRepository;
import com.ttn.e_commerce_project.respository.SellerRepository;
import com.ttn.e_commerce_project.service.AdminService;
import com.ttn.e_commerce_project.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.ttn.e_commerce_project.constants.UserConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminServiceImpl implements AdminService {

    CustomerRepository customerRepository;
    SellerRepository sellerRepository;
    EmailService emailService;
    UserCommonService userCommonService;
    CategoryMetadataFieldRepository categoryMetadataRepo;
    CategoryRepository categoryRepo;

    public Page<CustomerVo> listAllCustomers(int pageSize, int pageOffset, String sort, String email) {
        Pageable pageable = PageRequest.of(pageOffset, pageSize, Sort.by(sort));
        return customerRepository.findAllCustomers(email, pageable);
    }

    public Page<SellerVo> listAllSellers(int pageSize, int pageOffset, String sort, String email) {

        Pageable pageable = PageRequest.of(pageOffset, pageSize, Sort.by(sort));
        Page<SellerFlatVo> sellerPage = sellerRepository.findAllSellers(email, pageable);

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
            throw new ResourceNotFoundException(CUSTOMER_DOES_NOT_EXIST + id);
        else {
            updated = customerRepository.activateCustomerIfNotActive(id);
            if (updated == 1) {
                String email = userCommonService.findUserEmailById(id);
                Customer customer = userCommonService.findCustomerByEmail(email);
                customer.getUser().setPasswordUpdateDate(ZonedDateTime.now());
                customerRepository.save(customer);
                emailService.sendAcknowledgementMail(email, CUSTOMER_ACTIVATION_MAIL);
            }
            return (updated == 1);
        }
    }

    public boolean activateSeller(Long id) {
        int updated;
        if (!sellerRepository.existsById(id))
            throw new ResourceNotFoundException(SELLER_DOES_NOT_EXIST + id);
        else {
            updated = sellerRepository.activateSellerIfNotActive(id);
            System.out.println(updated);
            if (updated == 1) {
                String email = userCommonService.findUserEmailById(id);
                Seller seller = sellerRepository.findByUserEmail(email).get();
                seller.getUser().setPasswordUpdateDate(ZonedDateTime.now());
                sellerRepository.save(seller);
                emailService.sendAcknowledgementMail(email, SELLER_ACTIVATION_MAIL);
            }
            return (updated == 1);
        }
    }

    public boolean deactivateCustomer(Long id) {
        int updated;
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException(CUSTOMER_DOES_NOT_EXIST + id);
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
            throw new ResourceNotFoundException(SELLER_DOES_NOT_EXIST + id);
        } else {
            updated = sellerRepository.deactivateSellerIfActive(id);
            if (updated == 1) {
                return true; // Successfully deactivated
            }
            return false; // Already deactivated
        }
    }

    // Phase 4

    public CategoryMetaDataField addMetaDataField(MetadataFieldCo metadataFieldCo) {

        categoryMetadataRepo.findByNameIgnoreCase(metadataFieldCo.getName()).ifPresent(field -> {
            throw new InvalidArgumentException(FIELD_NAME_ALREADY_EXISTS);
        });

        CategoryMetaDataField field = new CategoryMetaDataField();
        field.setName(metadataFieldCo.getName());
        return categoryMetadataRepo.save(field);
    }

    public Page<MetadataFieldVo> getAllMetadataFields(int offset, int max, String sortBy, String order, String query) {
        Sort sort = order.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(offset, max, sort);
        Page<CategoryMetaDataField> result;
        if (query != null && !query.trim().isEmpty())
            result = categoryMetadataRepo.findByNameContainingIgnoreCase(query, pageable);
        else
            result = categoryMetadataRepo.findAll(pageable);
        return result.map(field -> new MetadataFieldVo(field.getId(), field.getName()));
    }


    public CategoryVo addCategory(CategoryCo categoryCo) {
        String name = categoryCo.getName();
        Category parent =null;
        if(categoryCo.getParentId()!=null){
            parent = categoryRepo.findById(categoryCo.getParentId()).orElseThrow(()->new ResourceNotFoundException("parent category not found"));
        }
        // 1. root category unique
        if(parent ==null)
        {
            if(categoryRepo.existsByNameAndParentIsNull(name))
            {
                throw new InvalidArgumentException("Root category "+name+" already exists");
            }
        }else {
            // tree wide uniqueness
            Category root = findRoot(parent);
            if(categoryRepo.existsByNameAndParent(name, parent)){
                throw new InvalidArgumentException("category name \t"+name+" \t already exists in tree root \t "+root.getName());
            }
        }
        Category category = new Category();
        category.setName(name);
        category.setParent(parent);
        Category saved = categoryRepo.save(category);
        return new CategoryVo(saved.getId(), saved.getName(),"Category created successfully");
    }


    private Category findRoot(Category category) {
        return category.getParent()==null?category:findRoot(category.getParent());
    }

//    private boolean isNameExistsInTree(Long rootId,String name)
//    {
//        Queue<Long> queue = new LinkedList<>();
//        queue.add(rootId);
//        while (!queue.isEmpty()) {
//            Long currentId = queue.poll();
//
//            Category current = categoryRepo.findById(currentId)
//                    .orElseThrow(() -> new InvalidArgumentException("Category not found: " + currentId));
//
//            if (current.getName().equalsIgnoreCase(name)) {
//                return true;
//            }
//
//            List<Long> childIds = categoryRepo.findChildIdsByParentId(currentId);
//            queue.addAll(childIds);
//        }
//        return false;
//    }
}

