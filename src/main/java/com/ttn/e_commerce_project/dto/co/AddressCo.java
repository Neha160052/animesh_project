package com.ttn.e_commerce_project.dto.co;

import com.ttn.e_commerce_project.customvalidation.ValidName;
import com.ttn.e_commerce_project.enums.Label;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static com.ttn.e_commerce_project.constants.UserConstants.GLOBAL_LOCATION_REGEX;
import static com.ttn.e_commerce_project.constants.UserConstants.ZIPCODE_REGEX;

@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class AddressCo {

    @Pattern(regexp = GLOBAL_LOCATION_REGEX,message="{city.name.invalid}")
    @Size(min = 2, max = 60, message = "{city.size}")
    @NotBlank(message = "{city.required}")
    String city;

    @Pattern(regexp = GLOBAL_LOCATION_REGEX,message ="{state.name.invalid}")
    @Size(min = 2, max = 60, message = "{state.size}")
    @NotBlank(message = "{state.required}")
    String state;

    @Pattern(regexp = GLOBAL_LOCATION_REGEX,message = "{country.name.invalid}")
    @Size(min = 2, max = 60, message = "{country.size}")
    @NotBlank(message = "{country.required}")
    String country;

    @NotBlank(message = "{address.line.required}")
    @Size(min=2,max=60,message="{address.line.size}")
    String addressLine;

    @NotBlank(message = "{zipcode.required}")
    @Pattern(regexp = ZIPCODE_REGEX,message = "{zipcode.invalid.format}")
    String zipCode;

    @Enumerated(EnumType.STRING)
    Label label;
}
