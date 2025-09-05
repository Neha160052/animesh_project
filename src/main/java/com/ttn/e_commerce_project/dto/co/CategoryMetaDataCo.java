package com.ttn.e_commerce_project.dto.co;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level =AccessLevel.PRIVATE)
public class CategoryMetaDataCo {

    @NotNull(message = "Category ID cannot be blank")
    Long categoryId;

    @NotNull(message = "Metadata Field ID cannot be blank")
    Long metaDataFieldId;

    @NotEmpty(message = "Field values cannot be blank")
    List<String> fieldValues;

}
