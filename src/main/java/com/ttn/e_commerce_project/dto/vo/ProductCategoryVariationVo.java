package com.ttn.e_commerce_project.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryVariationVo {

    @JsonProperty("productVariations")
    ProductVariationVo productVariationVo;
    @JsonProperty("category")
    CategoryVo categoryVo;
}
