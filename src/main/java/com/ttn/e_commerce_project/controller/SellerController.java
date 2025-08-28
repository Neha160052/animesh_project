package com.ttn.e_commerce_project.controller;

import com.ttn.e_commerce_project.dto.co.AddressCo;
import com.ttn.e_commerce_project.dto.co.SellerProfileCo;
import com.ttn.e_commerce_project.dto.co.UpdatePasswordCo;
import com.ttn.e_commerce_project.dto.vo.SellerProfileVo;
import com.ttn.e_commerce_project.service.SellerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/seller")
public class SellerController {

    SellerService sellerService;

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
}
