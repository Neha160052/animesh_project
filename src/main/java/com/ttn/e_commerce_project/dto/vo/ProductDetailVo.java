package com.ttn.e_commerce_project.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailVo {

     Long id;
     String name;
     String description;
     String brand;

    @JsonProperty("category")
    CategoryVo categoryVo;

    @JsonProperty("Variations")
    List<ProductVariationVo> variations;
}
