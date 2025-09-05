package com.ttn.e_commerce_project.dto.co;

import com.ttn.e_commerce_project.customvalidation.ValidName;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class ProductUpdateCo {

    @NotNull(message="{product.id.not.null}")
    Long productid;
    String name;
    String description;
    Boolean isCancellable;
    Boolean isReturnable;
}
