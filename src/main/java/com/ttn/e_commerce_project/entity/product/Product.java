package com.ttn.e_commerce_project.entity.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ttn.e_commerce_project.entity.audit.Auditable;
import com.ttn.e_commerce_project.entity.category.Category;
import com.ttn.e_commerce_project.entity.user.Seller;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE product SET is_deleted= true WHERE id=?")
public class Product extends Auditable {

       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       Long id;
       String name;
       String description;
       boolean isCancellable;
       boolean isReturnable;
       String brand;
       boolean isActive;
       boolean isDeleted;
       @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
       Set<ProductVariation> productVariation;
       @OneToMany
       @JoinColumn(name = "Product_id",referencedColumnName = "id")
       List<ProductReview> productReview;

       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(name = "category_id")
       Category category;

       @ManyToOne
       @JoinColumn(name = "seller_id")
       Seller seller;
 }
