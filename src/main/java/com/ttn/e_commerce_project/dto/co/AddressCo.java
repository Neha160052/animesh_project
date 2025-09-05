package com.ttn.e_commerce_project.dto.co;

import com.ttn.e_commerce_project.customvalidation.ValidName;
import com.ttn.e_commerce_project.enums.Label;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class AddressCo {

    @ValidName(message="{city.name.invalid}")
    @NotBlank
    String city;
    @ValidName(message ="{state.name.invalid}")
    @NotBlank
    String state;
    @ValidName(message = "{country.name.invalid}")
    @NotBlank(message = "address cannot be blank")
    String country;
    @NotBlank(message = "address line cannot be left blank")
    String addressLine;
    @NotNull(message = "zip code cannot be null")
    @Pattern(regexp = "^\\d{6}$",message = "Enter a valid zip code")
    int zipCode;
    @Enumerated(EnumType.STRING)
    Label label;
}
