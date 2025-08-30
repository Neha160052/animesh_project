package com.ttn.e_commerce_project.controller;

import com.ttn.e_commerce_project.dto.co.AddressCo;
import com.ttn.e_commerce_project.dto.co.SellerProfileCo;
import com.ttn.e_commerce_project.dto.co.UpdatePasswordCo;
import com.ttn.e_commerce_project.dto.vo.SellerProfileVo;
import com.ttn.e_commerce_project.service.SellerService;
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
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/seller")
public class SellerController {

    SellerService sellerService;
    ImageStorageUtil imageStorageUtil;

    @GetMapping("/profile")
    public ResponseEntity<SellerProfileVo> getMyProfile(Authentication authentication) {
        String email = authentication.getName(); // comes from JWT
        return ResponseEntity.ok(sellerService.getSellerProfile(email));
    }

    @PatchMapping("/update/profile")
    public ResponseEntity<String> patchMyProfile(Authentication auth,
                                                 @Valid @RequestBody SellerProfileCo sellerProfileCo)
    {
        sellerService.updateMyProfile(auth.getName(), sellerProfileCo);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @PatchMapping("/update-password")
    public ResponseEntity<String> updateSellerPassword(Authentication authentication,
                                                 @Valid @RequestBody UpdatePasswordCo updatePasswordCo) {

        sellerService.updatePassword(authentication.getName(), updatePasswordCo);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PatchMapping("/address/{id}")
    public ResponseEntity<String> updateAddress(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody AddressCo addressCo) {

        sellerService.updateAddress(authentication.getName(), id, addressCo);

        return ResponseEntity.ok("Address updated successfully");
    }
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file,
                                                Authentication  authentication) throws IOException, RoleNotFoundException {
        sellerService.checkOwnership(id, authentication.getName());
        String role = authentication.getAuthorities().stream()
                .findFirst().map(auth->
                        auth.getAuthority().replace("ROLE_","").toLowerCase(Locale.ROOT)).orElseThrow(RoleNotFoundException::new);
        String path = imageStorageUtil.saveImage(role, id, file);
        return ResponseEntity.ok("Image uploaded successfully at " + path);
    }

    @GetMapping("{id}/get-profile-image")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long id,Authentication authentication) throws IOException, RoleNotFoundException {
        sellerService.checkOwnership(id, authentication.getName());
        String role = authentication.getAuthorities().stream()
                .findFirst().map(auth->
                        auth.getAuthority().replace("ROLE_","").toLowerCase(Locale.ROOT)).orElseThrow(RoleNotFoundException::new);
        byte[] arr = imageStorageUtil.loadImage(role, id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(arr);
    }
}
