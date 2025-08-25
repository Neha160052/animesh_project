package com.ttn.e_commerce_project.entity.category;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class CategoryMetaDataField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    @OneToMany
    @JoinColumn(name = "category_metadata_field_id",referencedColumnName ="id" )
    Set<CategoryMetaDataValues> categoryMetaDataValues;
}
