package com.ttn.e_commerce_project.entities.address;

import com.ttn.e_commerce_project.enums.Label;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;



@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressFields {

    String city;
    String state;
    String country;
    String addressLine;
    int zipCode;
    @Enumerated
    Label label;
}
