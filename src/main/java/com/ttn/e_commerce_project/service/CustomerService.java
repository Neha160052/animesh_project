package com.ttn.e_commerce_project.service;

import com.ttn.e_commerce_project.dto.co.CustomerCo;
import org.springframework.http.ResponseEntity;

public interface CustomerService {

    ResponseEntity<String> register(CustomerCo customerCo);

}
