package com.ttn.e_commerce_project.entities.product;

import com.ttn.e_commerce_project.enums.Rating;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String review;

    @Enumerated
    Rating rating;

}
