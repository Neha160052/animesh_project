package com.ttn.e_commerce_project.dto.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerProductVo {
    String name;
    String brand;
    String description;
    CategoryVo categoryVo;
}
