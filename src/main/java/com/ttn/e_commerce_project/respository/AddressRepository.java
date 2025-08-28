package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByIdAndUserId(Long id, long id1);
}
