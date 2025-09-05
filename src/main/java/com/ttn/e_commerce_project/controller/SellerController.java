package com.ttn.e_commerce_project.controller;

import com.ttn.e_commerce_project.dto.co.*;
import com.ttn.e_commerce_project.dto.vo.SellerListCategoryVo;
import com.ttn.e_commerce_project.dto.vo.SellerProductVo;
import com.ttn.e_commerce_project.dto.vo.SellerProfileVo;
import com.ttn.e_commerce_project.entity.product.Product;
import com.ttn.e_commerce_project.entity.user.Seller;
import com.ttn.e_commerce_project.service.CategoryService;
import com.ttn.e_commerce_project.service.ProductService;
import com.ttn.e_commerce_project.service.SellerService;
import com.ttn.e_commerce_project.util.ImageStorageUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import static com.ttn.e_commerce_project.constants.UserConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/seller")
public class SellerController {

    SellerService sellerService;
    ImageStorageUtil imageStorageUtil;
    CategoryService categoryService;
    ProductService productService;

    @GetMapping("/profile")
    public ResponseEntity<SellerProfileVo> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(sellerService.getSellerProfile(email));
    }

    @PatchMapping("/update/profile")
    public ResponseEntity<String> patchMyProfile(Authentication auth,
                                                 @Valid @RequestBody SellerProfileCo sellerProfileCo)
    {
        sellerService.updateMyProfile(auth.getName(), sellerProfileCo);
        return ResponseEntity.ok(PROFILE_UPDATED);
    }

    @PatchMapping("/update-password")
    public ResponseEntity<String> updateSellerPassword(Authentication authentication,
                                                 @Valid @RequestBody UpdatePasswordCo updatePasswordCo) {

        sellerService.updatePassword(authentication.getName(), updatePasswordCo);
        return ResponseEntity.ok(PASSWORD_UPDATED);
    }

    @PatchMapping("/address/{id}")
    public ResponseEntity<String> updateAddress(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody AddressCo addressCo) {

        sellerService.updateAddress(authentication.getName(), id, addressCo);

        return ResponseEntity.ok(ADDRESS_UPDATED);
    }
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file,
                                                Authentication  authentication) throws IOException, RoleNotFoundException {
        sellerService.checkOwnership(id, authentication.getName());
        String role = authentication.getAuthorities().stream()
                .findFirst().map(auth->
                        auth.getAuthority().replace(ROLE_PREFIX,"").toLowerCase(Locale.ROOT)).orElseThrow(RoleNotFoundException::new);
        String path = imageStorageUtil.saveImage(role, id, file);
        return ResponseEntity.ok(IMAGE_UPLOADED + path);
    }

    @GetMapping("{id}/get-profile-image")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long id,Authentication authentication) throws IOException, RoleNotFoundException {
        sellerService.checkOwnership(id, authentication.getName());
        String role = authentication.getAuthorities().stream()
                .findFirst().map(auth->
                        auth.getAuthority().replace(ROLE_PREFIX,"").toLowerCase(Locale.ROOT)).orElseThrow(RoleNotFoundException::new);
        byte[] arr = imageStorageUtil.loadImage(role, id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(arr);
    }

    @GetMapping("/get-all-categories")
    public ResponseEntity<List<SellerListCategoryVo>> getAllLeafCategories() {
        List<SellerListCategoryVo> categories = categoryService.getAllLeafCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/add-products")
    public ResponseEntity<String> addProduct(@Valid @RequestBody ProductCo productCo){
        Product p = productService.addProduct(productCo);
        return ResponseEntity.ok(String.format(PRODUCT_SAVED_SUCCESSFULLY,p.getId()));
    }

    @GetMapping("/view-product/{id}")
    public ResponseEntity<SellerProductVo> viewProduct(@PathVariable @Valid Long id){
        return ResponseEntity.ok(productService.viewProduct(id));
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable @Valid Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok(PRODUCT_DELETED_SUCCESSFULLY);
    }

    @GetMapping("/view-all-products")
    public ResponseEntity<Page<SellerProductVo>> viewAllProducts(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order)
    {
        Pageable pageable = PageRequest.of(page, size,
                order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<SellerProductVo> products = productService.viewAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @PatchMapping("/update-product")
    public ResponseEntity<String> updateProduct(@Valid @RequestBody ProductUpdateCo productUpdateCo)
    {
        log.info(productUpdateCo.getProductid().toString());
       productService.updateProduct(productUpdateCo);
       return ResponseEntity.ok(PRODUCT_UPDATED_SUCCESSFULLY);
    }

    @PostMapping("/add-productvariation")
    public ResponseEntity<String> addProductVariation(@Valid @ModelAttribute ProductVariationCo variationCo) throws IOException {
        productService.addProductVariation(variationCo);
        return ResponseEntity.ok(PRODUCT_VARIATION_ADDED_SUCCESSFULLY);
    }

    @PatchMapping("/update-productvariation")
    public ResponseEntity<String> updateProductVariation(@Valid @ModelAttribute UpdateVariationCo variationCo) throws IOException {
        productService.updateProductVariation(variationCo);
        return  ResponseEntity.ok(PRODUCT_VARIATION_UPDATED_SUCCESSFULLY);
    }
    @GetMapping("/view-productvariation/{id}")
    public ResponseEntity<ProductVariationVo> viewProductVariation(@PathVariable Long id,Principal principal) throws IOException {
        String email = principal.getName();
        ProductVariationVo vo = productService.viewProductVariation(id,email);
        return  ResponseEntity.ok(vo);
    }

    @GetMapping("/view-all-variations/{productId}")
    public ResponseEntity<Page<ProductVariationVo>> viewAllProductVariations(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "10") int max,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction order,
            @RequestParam(required = false) String query,
            Principal principal) {

        Pageable pageable = PageRequest.of(offset, max, Sort.by(order, sort));

        String userEmail = principal.getName();

        Page<ProductVariationVo> variationPage = productService.viewAllVariationsForProduct(
                                                  productId, userEmail, query, pageable);

        return ResponseEntity.ok(variationPage);
    }

    // TODO : BUILD AN ENDPOINT FOR THIS CONTROLLER

    @GetMapping("/primary-product-image/{id}")
    public ResponseEntity<byte[]> viewProductImage(@PathVariable Long id ) throws IOException {
        byte[] arr = imageStorageUtil.loadImage("product", id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(arr);
    }

}
