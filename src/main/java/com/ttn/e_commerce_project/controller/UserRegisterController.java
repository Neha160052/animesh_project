package com.ttn.e_commerce_project.controller;

import com.ttn.e_commerce_project.dto.co.CustomerCo;
import com.ttn.e_commerce_project.dto.co.SellerCo;
import com.ttn.e_commerce_project.service.impl.CustomerServiceImpl;
import com.ttn.e_commerce_project.service.impl.SellerServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ttn.e_commerce_project.constants.UserConstants.CUSTOMER_REGISTERED_SUCCESSFULLY;
import static com.ttn.e_commerce_project.constants.UserConstants.SELLER_REGISTERED_SUCCESSFULLY;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/register")
public class UserRegisterController {

    CustomerServiceImpl customerService;
    SellerServiceImpl sellerService;

    @PostMapping("/customer")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerCo customerCo) {
        customerService.register(customerCo);
        return ResponseEntity.ok(CUSTOMER_REGISTERED_SUCCESSFULLY);
    }

    @PostMapping("/seller")
    public ResponseEntity<String> registerSeller(@Valid @RequestBody SellerCo sellerCo) {
        sellerService.register(sellerCo);
        return ResponseEntity.ok(SELLER_REGISTERED_SUCCESSFULLY);
    }

}

