package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.product.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductVariationRepository extends JpaRepository<ProductVariation,Long>, JpaSpecificationExecutor<ProductVariation> {
}
