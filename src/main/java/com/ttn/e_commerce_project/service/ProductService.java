package com.ttn.e_commerce_project.service;

import com.ttn.e_commerce_project.dto.co.ProductCo;
import com.ttn.e_commerce_project.dto.co.ProductUpdateCo;
import com.ttn.e_commerce_project.dto.co.ProductVariationCo;
import com.ttn.e_commerce_project.dto.co.UpdateVariationCo;
import com.ttn.e_commerce_project.dto.vo.ProductCategoryVariationVo;
import com.ttn.e_commerce_project.dto.vo.ProductDetailVo;
import com.ttn.e_commerce_project.dto.vo.ProductVariationVo;
import com.ttn.e_commerce_project.dto.vo.SellerProductVo;
import com.ttn.e_commerce_project.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    Product addProduct(ProductCo productCo);
    boolean activateProduct(Long productid);
    boolean deactivateProduct(Long productid);
    SellerProductVo viewProduct(Long productid);
    void deleteProduct(Long productid);
    Page<SellerProductVo> viewAllProducts(Pageable pageable);
    void updateProduct(ProductUpdateCo productUpdateCo);
    void addProductVariation(ProductVariationCo co) throws IOException;
    void updateProductVariation(UpdateVariationCo co) throws IOException;
    ProductVariationVo viewProductVariation(Long variationId, String email);
    Page<ProductVariationVo> viewAllVariationsForProduct(
            Long productId, String userEmail, String query, Pageable pageable);
    List<ProductCategoryVariationVo> viewAllVariationsGenericForProduct(Long productId);
    Page<ProductCategoryVariationVo> viewAllVariationsForAllProduct(Long categoryId, String query, Pageable pageable);
    Page<ProductDetailVo> findSimilarProducts(Long productId,String query,Pageable pageable);
    Page<ProductDetailVo> viewAllProducts(String query, Pageable pageable);
}
