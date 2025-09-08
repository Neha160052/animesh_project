package com.ttn.e_commerce_project.dto.co;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryMetaDataUpdateCo {

    @NotNull(message = "category id cannot be null ")
    private Long categoryId;

    @NotEmpty(message = "update field list cannot be empty")
    private List<@Valid FieldUpdate> updates;

}
