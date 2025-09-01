package com.ttn.e_commerce_project.entity.user;
import com.ttn.e_commerce_project.entity.audit.Auditable;
import com.ttn.e_commerce_project.entity.cart.Cart;
import com.ttn.e_commerce_project.entity.product.ProductReview;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer extends Auditable {

    String contact;
    @Id
    long userid;

    @MapsId
    @OneToOne
    @JoinColumn(name = " user_id")
    User user;

    @OneToMany
    @JoinColumn(name = "customer_user_id",referencedColumnName = "user_id")
    List<ProductReview> productReview;

    @OneToMany
    @JoinColumn(name = "customer_user_id",referencedColumnName = "user_id")
    List<Cart> cart;

}
