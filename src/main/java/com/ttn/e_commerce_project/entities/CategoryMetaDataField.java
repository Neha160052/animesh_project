package com.ttn.e_commerce_project.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class CategoryMetaDataField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @OneToMany
    @JoinColumn(name = "category_metadata_field_id",referencedColumnName ="id" )
    private Set<CategoryMetaDataValues> categoryMetaDataValues;
}
