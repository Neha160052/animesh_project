package com.ttn.e_commerce_project.entity.product;

import com.ttn.e_commerce_project.entity.audit.Auditable;
import com.ttn.e_commerce_project.entity.cart.Cart;
import com.ttn.e_commerce_project.entity.order.OrderProduct;
import com.ttn.e_commerce_project.util.MapToJsonConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ConverterRegistrations;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProductVariation extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    long quantityAvailable;
    double price;
    @Column(columnDefinition = "json")
    @Convert(converter = MapToJsonConverter.class)
    Map<String, Object> metadata;

    String primaryImageName;

    boolean isActive;

    @OneToMany
    @JoinColumn(name = "product_variation_id",referencedColumnName = "id")
    List<Cart> cart;

    @OneToMany
    @JoinColumn(name = "product_variation_id",referencedColumnName = "id")
    Set<OrderProduct> orderProducts;

}
