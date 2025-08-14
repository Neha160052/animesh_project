package com.ttn.e_commerce_project.entities;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    Category parentCategory;

    @OneToMany
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    Set<CategoryMetaDataValues> categoryMetaDataValues;
}
