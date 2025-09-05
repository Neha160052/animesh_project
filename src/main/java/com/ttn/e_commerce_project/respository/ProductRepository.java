package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.category.Category;
import com.ttn.e_commerce_project.entity.product.Product;
import com.ttn.e_commerce_project.entity.user.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByNameAndBrandAndCategoryAndSeller(String name, String brand, Category category, Seller seller);
    boolean existsByNameAndBrandAndCategoryAndSellerUserIdAndIdNot(String name, String brand, Category category, Long sellerId,Long productId);
    Page<Product> findBySeller(Seller seller, Pageable pageable);
}
