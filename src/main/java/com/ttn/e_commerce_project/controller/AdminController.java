package com.ttn.e_commerce_project.controller;

import com.ttn.e_commerce_project.dto.vo.CustomerVo;
import com.ttn.e_commerce_project.dto.vo.SellerVo;
import com.ttn.e_commerce_project.service.impl.AdminService;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.ttn.e_commerce_project.constants.UserConstants.*;

@Validated
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin")
public class AdminController {

    AdminService adminService;

    //api to list all the customers
    @GetMapping("/list-customers")
    ResponseEntity<Page<CustomerVo>> getAllCustomers(@RequestParam(defaultValue = "10") int pageSize,
                                                            @RequestParam(defaultValue = "0") int pageOffset,
                                                            @RequestParam(defaultValue = "id") String sort,
                                                            @RequestParam(required = false) @Email(message=INVALID_EMAIL) String email)
    {
            Page<CustomerVo> response = adminService.listAllCustomers(pageSize, pageOffset, sort, email);
            return ResponseEntity.ok(response);
    }
    //api to list all the sellers

    @GetMapping("/list-sellers")
    ResponseEntity<Page<SellerVo>> getAllSellers(@RequestParam(defaultValue = "10") int pageSize,
                                                 @RequestParam(defaultValue = "0") int pageOffset,
                                                 @RequestParam(defaultValue = "id") String sort,
                                                 @RequestParam(required = false)@Email(message=INVALID_EMAIL) String email)
    {
        Page<SellerVo> response = adminService.listAllSellers(pageSize,pageOffset,sort,email);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/activate-customer/{id}")
    ResponseEntity<String> activateCustomer(@PathVariable Long id)
    {
        boolean update = adminService.activeCustomer(id);
        if(!update)
            return ResponseEntity.ok(CUSTOMER_ALREADY_ACTIVE);
        else
            return ResponseEntity.ok(CUSTOMER_ACTIVATED_SUCCESSFULLY);
    }
    @PatchMapping("/activate-seller/{id}")
    ResponseEntity<String> activateSeller(@PathVariable Long id)
    {
        boolean update = adminService.activateSeller(id);
        if(!update)
            return  ResponseEntity.ok(SELLER_ALREADY_ACTIVE);
        else
            return ResponseEntity.ok(SELLER_ACTIVATED_SUCCESSFULLY);
    }

    @PatchMapping("/deactivate-customer/{id}")
    public ResponseEntity<String> deactivateCustomer(@PathVariable Long id) {
        boolean deactivated = adminService.deactivateCustomer(id);
        if (deactivated) {
            return ResponseEntity.ok(CUSTOMER_DEACTIVATED_SUCCESSFULLY + id);
        } else {
            return ResponseEntity.ok(CUSTOMER_ALREADY_DEACTIVATED + id);
        }
    }

    @PatchMapping("/deactivate-seller/{id}")
    public ResponseEntity<String> deactivateSeller(@PathVariable Long id) {
        boolean deactivated = adminService.deactivateSeller(id);
        if (deactivated) {
            return ResponseEntity.ok(SELLER_DEACTIVATED_SUCCESSFULLY + id);
        } else {
            return ResponseEntity.ok(SELLER_ALREADY_DEACTIVATED + id);
        }
    }
}
