package com.ttn.e_commerce_project.dto.co;

import com.ttn.e_commerce_project.customvalidation.ValidName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCo {

    @NotBlank(message = "Product name is required")
//    @ValidName(message = "name of product should be valid")
    String name;

    @NotBlank(message = "Brand is required")
    @ValidName(message = "brand name should be valid")
    String brand;

    String description;

    Boolean isCancellable = false;
    Boolean isReturnable = false;

    @NotNull(message = "Category ID is required")
    Long categoryId;
}
