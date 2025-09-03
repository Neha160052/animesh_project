package com.ttn.e_commerce_project.entity.category;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ttn.e_commerce_project.entity.audit.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

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

//    @OneToMany
//    @JoinColumn(name = "category_id",referencedColumnName = "id")
//    Set<CategoryMetaDataValues> categoryMetaDataValues;
}
