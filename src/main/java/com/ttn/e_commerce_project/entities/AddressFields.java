package com.ttn.e_commerce_project.entities;

import jakarta.persistence.Embeddable;
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
    String label;
}
