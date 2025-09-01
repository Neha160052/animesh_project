package com.ttn.e_commerce_project.dto.co;

import com.ttn.e_commerce_project.customvalidation.ValidName;
import com.ttn.e_commerce_project.enums.Label;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class AddressCo {
    String city;
    String state;
    String country;
    String addressLine;
    int zipCode;
    @Enumerated(EnumType.STRING)
    Label label;
}
