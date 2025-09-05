package com.ttn.e_commerce_project.dto.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVariationVo {

    Long productId;
    String productName;
    String productBrand;

    Long id;
    Long quantityAvailable;
    Double price;
    Map<String, Object> metadata;
    String primaryImageName;
    Boolean isActive;

    Long productId;
    String productName;
    String productBrand;

}
