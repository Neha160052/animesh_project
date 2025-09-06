package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.category.Category;
import com.ttn.e_commerce_project.entity.product.Product;
import com.ttn.e_commerce_project.entity.user.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product,Long> , JpaSpecificationExecutor<Product>,ProductRepositoryCustom{
    boolean existsByNameAndBrandAndCategoryAndSeller(String name, String brand, Category category, Seller seller);
    boolean existsByNameAndBrandAndCategoryAndSellerUserIdAndIdNot(String name, String brand, Category category, Long sellerId,Long productId);
    Page<Product> findBySeller(Seller seller, Pageable pageable);
}
