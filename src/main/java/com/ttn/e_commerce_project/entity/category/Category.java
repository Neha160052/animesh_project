package com.ttn.e_commerce_project.entity.category;
import com.ttn.e_commerce_project.entity.audit.Auditable;
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
public class Category extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    Category parent;

//    @OneToMany
//    @JoinColumn(name = "category_id",referencedColumnName = "id")
//    Set<CategoryMetaDataValues> categoryMetaDataValues;
}
