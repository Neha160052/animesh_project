package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByNameAndBrandAndCategoryIdAndSellerId(String name,String brand,Long categoryId,Long sellerId);
}
