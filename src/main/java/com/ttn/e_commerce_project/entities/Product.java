package com.ttn.e_commerce_project.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Product {

       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       Long id;
       String name;
       String description;
       boolean isCancellable;
       boolean isReturnable;
       String brand;
       boolean isActive;
       boolean isDeleted;
       @OneToMany
       @JoinColumn(name = "Product_id",referencedColumnName = "id")
       Set<ProductVariation> productVariation;
       @OneToMany
       @JoinColumn(name = "Product_id",referencedColumnName = "id")
       List<ProductReview> productReview;
 }
