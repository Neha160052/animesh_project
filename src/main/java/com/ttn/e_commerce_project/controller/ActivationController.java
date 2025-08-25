package com.ttn.e_commerce_project.controller;

import com.ttn.e_commerce_project.service.impl.CustomerServiceImpl;
import com.ttn.e_commerce_project.service.impl.UserCommonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/activate")
public class ActivationController {

    UserCommonService customerService;

    @GetMapping
    public ResponseEntity<String> activate(@RequestParam String token) {
        return customerService.activateUser(token);
    }

    @GetMapping("/resendActivationLink")
    public ResponseEntity<String> reSendActivationToken(@RequestParam String email) {
        customerService.resendActivationLink(email);
        return ResponseEntity.ok("link sent");
    }
}
