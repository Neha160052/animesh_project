package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.ProductCo;
import com.ttn.e_commerce_project.dto.co.ProductUpdateCo;
import com.ttn.e_commerce_project.dto.vo.CategoryVo;
import com.ttn.e_commerce_project.dto.vo.SellerProductVo;
import com.ttn.e_commerce_project.entity.category.Category;
import com.ttn.e_commerce_project.entity.product.Product;
import com.ttn.e_commerce_project.entity.user.Seller;
import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.exceptionhandling.ProductOwnershipException;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.CategoryRepository;
import com.ttn.e_commerce_project.respository.ProductRepository;
import com.ttn.e_commerce_project.service.EmailService;
import com.ttn.e_commerce_project.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        
        seller.getProduct().add(product);
        sellerRepo.save(seller);
        Product savedProduct = productRepo.save(product);
        log.info(savedProduct.getName());
        sellerRepo.save(seller);
        emailService.sendAcknowledgementMail(null, PRODUCT_ACTIVATION_MAIL);
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
       return products.map(this::mapToVo);
    }

    @Override
    public void updateProduct(ProductUpdateCo productUpdateCo) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = commonService.findSellerByEmail(email);
        Product product = commonService.findProductById(productUpdateCo.getProductid());
        if(product.getSeller().getUserid()!=(seller.getUserid()))
            throw new ProductOwnershipException(PRODUCT_DOES_NOT_BELONG_TO_USER);

        boolean exists = productRepo.existsByNameAndBrandAndCategoryAndSeller(
                productUpdateCo.getName(),
                product.getBrand(),
                product.getCategory(),
                seller);
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
            throw new InvalidArgumentException("Invalid metadata JSON format", e);
        }
        if (metadataMap.isEmpty()) {
            throw new InvalidArgumentException("Variation must have at least one metadata field-value");
        }
        ProductVariation variation = new ProductVariation();
        variation.setQuantityAvailable(co.getQuantityAvailable());
        variation.setPrice(co.getPrice());
        variation.setMetadata(metadataMap);
        variation.setActive(true);
        variation.setProduct(product);
        productVariationRepo.save(variation);
        saveProductImage(variation,co);
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


}
