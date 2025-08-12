package com.ttn.e_commerce_project.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class CategoryMetaDataField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany
    @JoinColumn(name = "category_metadata_field_id",referencedColumnName ="id" )
    private Set<CategoryMetaDataValues> categoryMetaDataValues;
}
