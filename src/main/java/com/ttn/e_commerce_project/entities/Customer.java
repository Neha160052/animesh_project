package com.ttn.e_commerce_project.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Customer {

    private Long contact;

    @Id
    @OneToOne
    @JoinColumn(name = " user_id")
    private User user;

    @OneToMany
    @JoinColumn(name = "customer_user_id",referencedColumnName = "user_id")
    private List<ProductReview> productReview;
}
