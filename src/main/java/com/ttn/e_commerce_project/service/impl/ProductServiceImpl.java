package com.ttn.e_commerce_project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttn.e_commerce_project.dto.co.ProductCo;
import com.ttn.e_commerce_project.dto.co.ProductUpdateCo;
import com.ttn.e_commerce_project.dto.co.ProductVariationCo;
import com.ttn.e_commerce_project.dto.co.UpdateVariationCo;
import com.ttn.e_commerce_project.dto.vo.CategoryVo;
import com.ttn.e_commerce_project.dto.vo.ProductVariationVo;
import com.ttn.e_commerce_project.dto.vo.SellerProductVo;
import com.ttn.e_commerce_project.entity.category.Category;
import com.ttn.e_commerce_project.entity.category.CategoryMetaDataValues;
import com.ttn.e_commerce_project.entity.product.Product;
import com.ttn.e_commerce_project.entity.product.ProductVariation;
import com.ttn.e_commerce_project.entity.user.Seller;
import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.exceptionhandling.ProductOwnershipException;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.CategoryRepository;
import com.ttn.e_commerce_project.respository.ProductRepository;
import com.ttn.e_commerce_project.respository.ProductVariationRepository;
import com.ttn.e_commerce_project.service.EmailService;
import com.ttn.e_commerce_project.service.ProductService;
import com.ttn.e_commerce_project.util.ImageStorageUtil;
import com.ttn.e_commerce_project.util.ProductVariationSpecification;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    ObjectMapper objectMapper;
    ProductVariationRepository  productVariationRepo;
    ImageStorageUtil imageStorageUtil;

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
        emailService.sendAcknowledgementMail(null, PRODUCT_ACTIVATION_MAIL);
        return savedProduct;
    }

    @Override
    public boolean activateProduct(Long productid) {
        Product product = commonService.findProductById(productid);
        if (!product.isActive()) {
            product.setActive(true);
            productRepo.save(product);
            return true;
        } else
            return false;
    }

    @Override
    public boolean deactivateProduct(Long productid) {
        Product product = commonService.findProductById(productid);
        if (product.isActive()) {
            product.setActive(false);
            productRepo.save(product);
            return true;
        } else
            return false;
    }

    @Override
    public SellerProductVo viewProduct(Long productid) {
        Product product = commonService.findProductById(productid);
        return mapToVo(product);
    }

    @Override
    public void deleteProduct(Long productid) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = commonService.findSellerByEmail(email);
        Product product = commonService.findProductById(productid);
        if(product.getSeller().getUserid()!=(seller.getUserid()))
            throw new ProductOwnershipException(PRODUCT_DOES_NOT_BELONG_TO_USER);
        productRepo.delete(product);
    }

    public Page<SellerProductVo> viewAllProducts(Pageable pageable)
    {
       String email = SecurityContextHolder.getContext().getAuthentication().getName();
       Seller seller = commonService.findSellerByEmail(email);
       Page<Product> products = productRepo.findBySeller(seller,pageable);
        log.info(products.getContent().toString());
       return products.map(this::mapToVo);
    }

    @Override
    public void updateProduct(ProductUpdateCo productUpdateCo) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = commonService.findSellerByEmail(email);
        log.info(productUpdateCo.getProductid().toString());
        Product product = commonService.findProductById(productUpdateCo.getProductid());
        if(product.getSeller().getUserid()!=(seller.getUserid()))
            throw new ProductOwnershipException(PRODUCT_DOES_NOT_BELONG_TO_USER);

        boolean exists = productRepo.existsByNameAndBrandAndCategoryAndSellerUserIdAndIdNot(
                productUpdateCo.getName(),
                product.getBrand(),
                product.getCategory(),
                seller.getUserid(),
                product.getId());
        if (exists) {
            throw new InvalidArgumentException(PRODUCT_ALREADY_EXISTS);
        }
        if(productUpdateCo.getName()!=null)
            product.setName(productUpdateCo.getName());
        if(productUpdateCo.getDescription()!=null)
            product.setDescription(productUpdateCo.getDescription());
        if((productUpdateCo.getIsCancellable()!=null))
            product.setCancellable(productUpdateCo.getIsCancellable());
        if((productUpdateCo.getIsReturnable()!=null))
            product.setCancellable(productUpdateCo.getIsReturnable());
        productRepo.save(product);
    }

    public void addProductVariation(ProductVariationCo co) throws IOException {
       String email = SecurityContextHolder.getContext().getAuthentication().getName();
       Seller seller = commonService.findSellerByEmail(email);
       Product product = commonService.findProductById(co.getProductId());
       if(seller.getUserid()!=product.getSeller().getUserid())
           throw new ProductOwnershipException(PRODUCT_DOES_NOT_BELONG_TO_USER);

       if(product.isDeleted()||!product.isActive())
           throw new InvalidArgumentException("Product is either deleted or inactive");

        Map<String, Object> metadataMap;
        try {
            metadataMap = objectMapper.readValue(co.getMetadata(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new InvalidArgumentException("Invalid metadata JSON format");
        }
        if (metadataMap.isEmpty()) {
            throw new InvalidArgumentException("Variation must have at least one metadata field-value");
        }
        ProductVariation variation = new ProductVariation();
        variation.setQuantityAvailable(co.getQuantityAvailable());
        variation.setPrice(co.getPrice());
        validateAndSetMetadata(product, variation, co.getMetadata());
        variation.setActive(true);
        variation.setProduct(product);
        productVariationRepo.save(variation);
        saveProductImage(variation,co);
    }

    @Override
    public void updateProductVariation(UpdateVariationCo co) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = commonService.findSellerByEmail(email);
        ProductVariation variation= productVariationRepo.findById(co.getVariationId())
                              .orElseThrow(()->new ResourceNotFoundException(PRODUCT_VARIATION_NOT_FOUND));

        if(seller.getUserid()!=variation.getProduct().getSeller().getUserid())
            throw new ProductOwnershipException(VARIATION_DOES_NOT_BELONG);

        Product product = variation.getProduct();

        if(!product.isActive()|| product.isDeleted())
            throw new InvalidArgumentException(PRODUCT_NOT_ACTIVE_OR_DELETED);

        if (co.getQuantityAvailable() != null) {
            variation.setQuantityAvailable(co.getQuantityAvailable());
        }
        if (co.getPrice() != null) {
            variation.setPrice(co.getPrice());
        }
        if (co.getIsActive() != null) {
            variation.setActive(co.getIsActive());
        }
        productVariationRepo.save(variation);
        validateAndSetMetadata(product,variation,co.getMetadata());
        updateProductImage(variation,co);

    }

    private void saveProductImage(ProductVariation variation,ProductVariationCo co) throws IOException {

        String primaryImageName = null;
        if (co.getPrimaryImage() != null && !co.getPrimaryImage().isEmpty()) {
            primaryImageName = imageStorageUtil.saveImage("products", String.valueOf(variation.getId()), co.getPrimaryImage());

            List<String> secondaryImageNames = new ArrayList<>();
            if (co.getSecondaryImage() != null) {
                int index = 0;
                for (MultipartFile file : co.getSecondaryImage()) {
                    if (!file.isEmpty()) {
                        String secondaryId = imageStorageUtil.buildSecondaryImageName(variation.getId(), index++);
                        secondaryImageNames.add(imageStorageUtil.saveImage("secondary_images",secondaryId,file ));
                    }}}
            variation.setPrimaryImageName(primaryImageName);
            productVariationRepo.save(variation);
        }
    }

    private void updateProductImage(ProductVariation variation,UpdateVariationCo co) throws IOException {

        String primaryImageName = null;
        if (co.getPrimaryImage() != null && !co.getPrimaryImage().isEmpty()) {
            primaryImageName = imageStorageUtil.saveImage("products", String.valueOf(variation.getId()), co.getPrimaryImage());

            List<String> secondaryImageNames = new ArrayList<>();
            if (co.getSecondaryImage() != null) {
                int index = 0;
                for (MultipartFile file : co.getSecondaryImage()) {
                    if (!file.isEmpty()) {
                        String secondaryId = imageStorageUtil.buildSecondaryImageName(variation.getId(), index++);
                        secondaryImageNames.add(imageStorageUtil.saveImage("secondary_images",secondaryId,file ));
                    }}}
            variation.setPrimaryImageName(primaryImageName);
            productVariationRepo.save(variation);
        }
    }

    private SellerProductVo mapToVo(Product product) {
        SellerProductVo vo = new SellerProductVo();
        vo.setId(product.getId());
        vo.setBrand(product.getBrand());
        vo.setDescription(product.getDescription());
        vo.setName(product.getName());
        Category category = product.getCategory();
        if (category != null) {
            vo.setCategory(new CategoryVo(category.getId(), category.getName(), null));
        }
        return vo;
    }

    @Override
    public ProductVariationVo viewProductVariation(Long variationId, String email)
    {
        Seller seller = commonService.findSellerByEmail(email);
        ProductVariation variation = productVariationRepo.findById(variationId).orElseThrow(()->new ResourceNotFoundException(PRODUCT_VARIATION_NOT_FOUND));
        if(variation.getProduct().getSeller().getUserid()!=seller.getUserid())
            throw new ProductOwnershipException(PRODUCT_DOES_NOT_BELONG_TO_USER);
        return mapToVariationVo(variation);
    }

    @Override
    public Page<ProductVariationVo> viewAllVariationsForProduct(Long productId, String userEmail, String query, Pageable pageable) {
        Seller seller = commonService.findSellerByEmail(userEmail);
        Product product = commonService.findProductById(productId);
        if(!product.isActive()|| product.isDeleted())
            throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
        if(product.getSeller().getUserid()!= seller.getUserid())
            throw new ProductOwnershipException(PRODUCT_DOES_NOT_BELONG_TO_USER);
        Specification<ProductVariation> spec = ProductVariationSpecification.belongsToProduct(productId)
                                                           .and(ProductVariationSpecification.filterByQuery(query));
        Page<ProductVariation> variationPage = productVariationRepo.findAll(spec, pageable);
        return variationPage.map(this::mapToVariationVo);
    }

    @Override
    public List<ProductCategoryVariationVo> viewAllVariationsGenericForProduct(Long productId) {
        Product product = commonService.findProductById(productId);
        if(!product.isActive())
            throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
        Set<ProductVariation> variations = product.getProductVariation();
        return variations.stream()
                .map(this::mapToProductCategoryVariationVo).toList();
    }

    @Override
    public Page<ProductCategoryVariationVo> viewAllVariationsForAllProduct(Long categoryId, String query, Pageable pageable) {

        Category category = categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException(CATEGORY_NOT_FOUND));

        if(!category.isLeaf())
            throw new InvalidArgumentException(CATEGORY_MUST_BE_LEAF);

        Specification<ProductVariation> spec = ProductVariationSpecification.belongsToCategory(categoryId)
                                                      .and(ProductVariationSpecification.filterByQuery(query));

        Page<ProductVariation> variationPage = productVariationRepo.findAll(spec, pageable);

        return variationPage.map(this::mapToProductCategoryVariationVo);
    }



    private ProductCategoryVariationVo mapToProductCategoryVariationVo(ProductVariation variation) {
        ProductVariationVo variationVo = mapToVariationVo(variation);
        Category category = variation.getProduct().getCategory();
        CategoryVo categoryVo = new CategoryVo(category.getId(), category.getName(), null);
        return new ProductCategoryVariationVo(variationVo, categoryVo);
    }

    private ProductVariationVo mapToVariationVo(ProductVariation variation)
    {
        ProductVariationVo vo = new ProductVariationVo();
        vo.setId(variation.getId());
        vo.setQuantityAvailable(variation.getQuantityAvailable());
        vo.setPrice(variation.getPrice());
        vo.setMetadata(variation.getMetadata());
        vo.setPrimaryImageName(variation.getPrimaryImageName());
        vo.setIsActive(variation.isActive());
        vo.setProductId(variation.getProduct().getId());
        vo.setProductName(variation.getProduct().getName());
        vo.setProductBrand(variation.getProduct().getBrand());
        return vo;
    }

    private void validateAndSetMetadata(Product product, ProductVariation variation, String metadataJson) {
        log.info("Starting metadata validation for product id: {}", product.getId());

        if (metadataJson == null) {
            log.info("No metadata provided. Skipping validation.");
            return;
        }

        Map<String, Object> metadata;
        try {
            ObjectMapper mapper = new ObjectMapper();
            metadata = mapper.readValue(metadataJson, new TypeReference<Map<String, Object>>() {});
            log.info("Parsed metadata JSON: {}", metadata);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse metadata JSON: {}", metadataJson, e);
            throw new InvalidArgumentException("Invalid metadata JSON format");
        }

        String str = String.valueOf(product.getCategory().getCategoryMetaDataValues());
        log.info("str >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + str);
        Set<CategoryMetaDataValues> allowedValues = product.getCategory().getCategoryMetaDataValues();
        if (allowedValues == null || allowedValues.isEmpty()) {
            log.warn("Product category metadata is empty or null for product id: {}", product.getId());
            throw new InvalidArgumentException("No metadata defined for product category");
        }

        Map<String, Set<String>> allowedMap = new HashMap<>();
        for (CategoryMetaDataValues cmfv : allowedValues) {
            String fieldName = cmfv.getCategoryMetaDataField().getName().toLowerCase();
            Set<String> values = Arrays.stream(cmfv.getFieldValues().split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
            allowedMap.computeIfAbsent(fieldName, k -> new HashSet<>()).addAll(values);
            log.info("Allowed field (merged): {} -> {}", fieldName, allowedMap.get(fieldName));
        }

        for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            String key = entry.getKey().toLowerCase();
            String value = String.valueOf(entry.getValue()).toLowerCase();
            log.info("Validating metadata field: {} -> {}", key, value);

            if (!allowedMap.containsKey(key)) {
                log.error("Field '{}' not found in allowed fields: {}", key, allowedMap.keySet());
                throw new InvalidArgumentException("Field not found: " + key);
            }

            if (!allowedMap.get(key).contains(value)) {
                log.error("Value '{}' not allowed for field '{}'. Allowed values: {}", value, key, allowedMap.get(key));
                throw new InvalidArgumentException("Metadata value not allowed: " + value + " for field: " + key);
            }
        }
        variation.setMetadata(metadata);
        log.info("Metadata validation passed and set for product variation.");
    }

}
