package com.ttn.e_commerce_project.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProductVariation extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    long quantityAvailable;
    double price;
    @Column(columnDefinition = "json")
    String metadata;

    String primaryImageName;

    boolean isActive;

    @OneToMany
    @JoinColumn(name = "product_variation_id",referencedColumnName = "id")
    List<Cart> cart;

    @OneToMany
    @JoinColumn(name = "product_variation_id",referencedColumnName = "id")
    Set<OrderProduct> orderProducts;

}
