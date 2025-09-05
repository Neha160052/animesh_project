package com.ttn.e_commerce_project.entity.category;

import com.ttn.e_commerce_project.entity.audit.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryMetaDataValues extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String fieldValues;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne
    CategoryMetaDataField categoryMetaDataField;
}
