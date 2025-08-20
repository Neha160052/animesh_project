package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.user.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller,Long> {
}
