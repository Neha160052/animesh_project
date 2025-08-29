package com.ttn.e_commerce_project.controller;


import com.ttn.e_commerce_project.dto.co.AddressCo;
import com.ttn.e_commerce_project.dto.co.CustomerCo;
import com.ttn.e_commerce_project.dto.co.CustomerProfileCo;
import com.ttn.e_commerce_project.dto.co.UpdatePasswordCo;
import com.ttn.e_commerce_project.dto.vo.AddressVo;
import com.ttn.e_commerce_project.dto.vo.CustomerProfileVo;
import com.ttn.e_commerce_project.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CustomerController {

    CustomerService customerService;

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
    public ResponseEntity<String> updateMyProfile(Authentication authentication, @RequestBody CustomerProfileCo customerProfileCo)
    {
        String email = authentication.getName();
        String message = customerService.updateMyProfile(email,customerProfileCo);
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/update-password")
    public ResponseEntity<String> updateCustomerPassword(Authentication authentication,
                                                       @Valid @RequestBody UpdatePasswordCo updatePasswordCo) {

        customerService.updatePassword(authentication.getName(), updatePasswordCo);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PostMapping("/add-address")
    public ResponseEntity<String> addAddress(Authentication authentication,@RequestBody AddressCo addressCo ) {
        return ResponseEntity.ok(customerService.addCustomerAddress(authentication.getName(), addressCo));
    }

    @DeleteMapping("/delete/address/{id}")
    public ResponseEntity<String> deleteAddress(Authentication authentication ,@PathVariable Long id) {

        return ResponseEntity.ok(customerService.deleteAddress(authentication.getName(), id));
    }

    @PutMapping("/update/address/{id}")
    public ResponseEntity<String> updateAddress(Authentication authentication, @PathVariable Long id,
                                                @RequestBody AddressCo addressCo) {
        return ResponseEntity.ok(customerService.updateAddress(id, addressCo));
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file,
                                                                           Principal principal) throws IOException {
        // ✅ Security check: ensure logged-in user is the owner
        customerService.checkOwnership(id, principal.getName());
        // Save image
        String path = imageStorageUtil.saveImage("customer", id, file);
        return ResponseEntity.ok("Image uploaded successfully at " + path);
    }

    @GetMapping("{id}/get-profile-image")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long id,Authentication authentication) throws IOException, RoleNotFoundException {
        customerService.checkOwnership(id, authentication.getName());
        String role = authentication.getAuthorities().stream()
                .findFirst().map(auth->
                        auth.getAuthority().replace("ROLE_","")).orElseThrow(RoleNotFoundException::new);
        byte[] arr = imageStorageUtil.loadImage(role, id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(arr);
    }
}
