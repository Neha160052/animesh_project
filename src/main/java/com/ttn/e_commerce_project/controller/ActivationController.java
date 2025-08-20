package com.ttn.e_commerce_project.controller;

import com.ttn.e_commerce_project.entity.VerificationToken;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.helper.SetCustomerActiveHelper;
import com.ttn.e_commerce_project.respository.UserRepository;
import com.ttn.e_commerce_project.service.impl.VerificationTokenServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/activate")
public class ActivationController {

    CustomerServiceImpl customerService;

    @GetMapping
    public ResponseEntity<String> activate(@RequestParam("token") String token) {
        return setCustomerActiveHelper.activateCustomer(token);
    }

    @GetMapping("/resendToken")
    public ResponseEntity<String> reSendActivationToken(@RequestParam("token") String token) {
        return setCustomerActiveHelper.activateCustomer(token);
    }
}
