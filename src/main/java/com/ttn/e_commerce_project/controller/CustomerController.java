package com.ttn.e_commerce_project.controller;


import com.ttn.e_commerce_project.dto.co.AddressCo;
import com.ttn.e_commerce_project.dto.co.CustomerCo;
import com.ttn.e_commerce_project.dto.co.CustomerProfileCo;
import com.ttn.e_commerce_project.dto.co.UpdatePasswordCo;
import com.ttn.e_commerce_project.dto.vo.AddressVo;
import com.ttn.e_commerce_project.dto.vo.CustomerProfileVo;
import com.ttn.e_commerce_project.entity.category.Category;
import com.ttn.e_commerce_project.service.CategoryService;
import com.ttn.e_commerce_project.service.CustomerService;
import com.ttn.e_commerce_project.service.impl.UserCommonService;
import com.ttn.e_commerce_project.util.ImageStorageUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

import static com.ttn.e_commerce_project.constants.UserConstants.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CustomerController {

    CustomerService customerService;
    ImageStorageUtil imageStorageUtil;
    UserCommonService commonService;
    CategoryService categoryService;
    ProductService productService;

    @GetMapping("/profile")
    public ResponseEntity<CustomerProfileVo> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(customerService.getMyProfile(authentication.getName()));
    }

    @GetMapping("/list-addresses")
    public ResponseEntity<List<AddressVo>> getMyAddresses(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(customerService.getMyAddresses(email));
    }

    @PatchMapping("/update-profile")
    public ResponseEntity<String> updateMyProfile(Authentication authentication, @RequestBody CustomerProfileCo customerProfileCo) {
        String email = authentication.getName();
        String message = customerService.updateMyProfile(email, customerProfileCo);
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/update-password")
    public ResponseEntity<String> updateCustomerPassword(Authentication authentication,
                                                         @Valid @RequestBody UpdatePasswordCo updatePasswordCo) {

        customerService.updatePassword(authentication.getName(), updatePasswordCo);
        return ResponseEntity.ok(PASSWORD_UPDATED);
    }

    @PostMapping("/add-address")
    public ResponseEntity<String> addAddress(Authentication authentication, @RequestBody AddressCo addressCo) {
        return ResponseEntity.ok(customerService.addCustomerAddress(authentication.getName(), addressCo));
    }

    @DeleteMapping("/delete/address/{id}")
    public ResponseEntity<String> deleteAddress(Authentication authentication, @PathVariable Long id) {

        return ResponseEntity.ok(customerService.deleteAddress(authentication.getName(), id));
    }

    @PutMapping("/update/address/{id}")
    public ResponseEntity<String> updateAddress(Authentication authentication, @PathVariable Long id,
                                                @Valid @RequestBody AddressCo addressCo) {
        return ResponseEntity.ok(customerService.updateAddress(id, addressCo));
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file,
                                              Principal principal) throws IOException {
        customerService.checkOwnership(id, principal.getName());
        String path = imageStorageUtil.saveImage(CUSTOMER_USER_TYPE, id.toString(), file);
        return ResponseEntity.ok(IMAGE_UPLOADED+ path);
    }

    @GetMapping("{id}/get-profile-image")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long id,Authentication authentication) throws IOException, RoleNotFoundException {
        customerService.checkOwnership(id, authentication.getName());
        String role = authentication.getAuthorities().stream()
                .findFirst().map(auth->
                        auth.getAuthority().replace(ROLE_PREFIX,"").toLowerCase(Locale.ROOT)).orElseThrow(RoleNotFoundException::new);
        byte[] arr = imageStorageUtil.loadImage(role, id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(arr);
    }

    @GetMapping("/get-all-categories")
    public ResponseEntity<List<Category>> listCategories(
                                           @RequestParam(required = false) Long categoryId) {

        List<Category> categories = categoryService.getCategories(categoryId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/view-product-variations/{productId}")
    public ResponseEntity<List<ProductCategoryVariationVo>> viewAllProductVariations(@PathVariable Long productId) {
        List<ProductCategoryVariationVo> variationList = productService.viewAllVariationsGenericForProduct(productId);
        return ResponseEntity.ok(variationList);
    }

    @GetMapping("/view-all-variations/{categoryId}")
    public ResponseEntity<Page<ProductCategoryVariationVo>> viewAllProductAllVariations(@PathVariable Long categoryId,
                                                                                        @RequestParam(defaultValue = "10") int max,
                                                                                        @RequestParam(defaultValue = "0") int offset,
                                                                                        @RequestParam(defaultValue = "id") String sort,
                                                                                        @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                                                        @RequestParam(required = false) String query) {
        Pageable pageable = PageRequest.of(offset, max, Sort.by(order, sort));
        
        Page<ProductCategoryVariationVo> variationPage = productService.viewAllVariationsForAllProduct(categoryId,query,pageable);
        return ResponseEntity.ok(variationPage);
    }

    @GetMapping("/view-similar-products/{productId}")
    public ResponseEntity<Page<ProductDetailVo>> viewAllSimilarProducts(@PathVariable Long productId,
                                                                                        @RequestParam(defaultValue = "10") int max,
                                                                                        @RequestParam(defaultValue = "0") int offset,
                                                                                        @RequestParam(defaultValue = "id") String sort,
                                                                                        @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                                                        @RequestParam(required = false) String query) {
        Pageable pageable = PageRequest.of(offset, max, Sort.by(order, sort));

        Page<ProductDetailVo> similarProductsPage = productService.findSimilarProducts(productId,pageable);
        return ResponseEntity.ok(similarProductsPage);
    }

}
