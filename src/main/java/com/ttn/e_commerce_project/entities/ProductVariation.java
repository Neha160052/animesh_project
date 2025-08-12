package com.ttn.e_commerce_project.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    long quantityAvailable;
    double price;
    @Column(columnDefinition = "json")
    String metadata;

    String primaryImageName;

    boolean isActive;


}
