package com.ttn.e_commerce_project.dto.co;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FieldUpdate {

    @NotNull(message = "metadata field id cannot be null")
    Long metaDataFieldId;

    @NotEmpty(message ="list of values cannot be empty")
    List<@NotBlank String> values;

}
