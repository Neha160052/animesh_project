package com.ttn.e_commerce_project.entities;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Set<CategoryMetaDataValues> categoryMetaDataValues;
}
