package com.ttn.e_commerce_project.entity.category;

import com.ttn.e_commerce_project.entity.audit.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class CategoryMetaDataField extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
//    @OneToMany
//    @JoinColumn(name = "category_metadata_field_id",referencedColumnName ="id" )
//    Set<CategoryMetaDataValues> categoryMetaDataValues;
}
