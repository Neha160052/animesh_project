package com.ttn.e_commerce_project.service;

import com.ttn.e_commerce_project.dto.co.ProductCo;
import com.ttn.e_commerce_project.dto.vo.SellerProductVo;
import com.ttn.e_commerce_project.entity.product.Product;
import com.ttn.e_commerce_project.entity.user.Seller;

public interface ProductService {

    Product addProduct(ProductCo productCo);
    boolean activateProduct(Long productid);
    boolean deactivateProduct(Long productid);
    SellerProductVo viewProduct(Long productid);
}
