package com.ttn.e_commerce_project.controller;

import com.ttn.e_commerce_project.dto.co.CustomerCo;
import com.ttn.e_commerce_project.service.impl.CustomerServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/register")
public class UserController {

    CustomerServiceImpl customerService;
    SellerServiceImpl sellerService;

    @PostMapping("/customer")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerCo customerCo) {
        return customerService.register(customerCo);
    }
}

