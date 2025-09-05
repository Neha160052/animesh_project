package com.ttn.e_commerce_project.entity.category;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ttn.e_commerce_project.entity.audit.Auditable;
import com.ttn.e_commerce_project.entity.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    Category parent;

    @Transient
    @JsonManagedReference
    List<Category> children = new ArrayList<>();

    boolean isLeaf;

    @OneToMany
    Set<Product> products;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CategoryMetaDataValues> categoryMetaDataValues = new HashSet<>();
}
