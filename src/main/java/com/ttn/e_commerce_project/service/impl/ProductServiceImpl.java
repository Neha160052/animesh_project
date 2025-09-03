package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.ProductCo;
import com.ttn.e_commerce_project.dto.vo.CategoryVo;
import com.ttn.e_commerce_project.dto.vo.SellerProductVo;
import com.ttn.e_commerce_project.entity.category.Category;
import com.ttn.e_commerce_project.entity.product.Product;
import com.ttn.e_commerce_project.entity.user.Seller;
import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.CategoryRepository;
import com.ttn.e_commerce_project.respository.ProductRepository;
import com.ttn.e_commerce_project.service.EmailService;
import com.ttn.e_commerce_project.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.ttn.e_commerce_project.constants.UserConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    CategoryRepository categoryRepo;
    ProductRepository productRepo;
    EmailService emailService;
    UserCommonService commonService;

    @Override
    public Product addProduct(ProductCo productCo) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info(email);
        Seller seller = commonService.findSellerByEmail(email);
        Category category = categoryRepo.findById(productCo.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND + productCo.getCategoryId()));
        log.info(category.getName());

        if (!category.isLeaf())
            throw new InvalidArgumentException(CATEGORY_MUST_BE_LEAF);
        boolean exists = productRepo.existsByNameAndBrandAndCategoryAndSeller(
                productCo.getName(),
                productCo.getBrand(),
                category,
                seller);
        log.info(String.valueOf(exists));
        if (exists) {
            throw new InvalidArgumentException(PRODUCT_ALREADY_EXISTS);
        }
        Product product = new Product();
        product.setName(productCo.getName());
        product.setBrand(productCo.getBrand());
        if (productCo.getDescription() != null)
            product.setDescription(productCo.getDescription());
        product.setCancellable(Boolean.TRUE.equals(productCo.getIsCancellable()));
        product.setReturnable(Boolean.TRUE.equals(productCo.getIsReturnable()));
        product.setCategory(category);

        product.setSeller(seller);
        Product savedProduct = productRepo.save(product);
        log.info(savedProduct.getName());
        emailService.sendAcknowledgementMail(email, PRODUCT_ACTIVATION_MAIL);
        return savedProduct;
    }

    @Override
    public boolean activateProduct(Long productid) {
        Product product = commonService.findProductById(productid);
        if (!product.isActive()) {
            product.setActive(true);
            return true;
        } else
            return false;
    }

    @Override
    public boolean deactivateProduct(Long productid) {
        Product product = commonService.findProductById(productid);
        if (product.isActive()) {
            product.setActive(false);
            return true;
        } else
            return false;
    }

    @Override
    public SellerProductVo viewProduct(Long productid) {
        Product product = commonService.findProductById(productid);
        SellerProductVo sellerProductVo = new SellerProductVo();
        sellerProductVo.setBrand(product.getBrand());
        sellerProductVo.setDescription(product.getDescription());
        sellerProductVo.setName(product.getName());
        sellerProductVo.setCategoryVo(new CategoryVo(product.getCategory().getId()
                , product.getCategory().getName(), null));
        return sellerProductVo;
    }

}
