package com.ttn.e_commerce_project.controller;

import com.ttn.e_commerce_project.dto.co.AddressCo;
import com.ttn.e_commerce_project.dto.co.ProductCo;
import com.ttn.e_commerce_project.dto.co.SellerProfileCo;
import com.ttn.e_commerce_project.dto.co.UpdatePasswordCo;
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
}
