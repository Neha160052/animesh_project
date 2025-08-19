package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.dto.co.CustomerCo;
import com.ttn.e_commerce_project.entity.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    String register(CustomerCo customerCo);
}
