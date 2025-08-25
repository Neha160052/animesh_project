package com.ttn.e_commerce_project.entity.order;

import com.ttn.e_commerce_project.entity.audit.Auditable;
import com.ttn.e_commerce_project.enums.Label;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders")
public class Order extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    double amountPaid;
    ZonedDateTime dateCreated;

    String city;
    String state;
    String country;
    String addressLine;
    int zipCode;
    @Enumerated
    Label label;

    @OneToMany
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    Set<OrderProduct> orderProducts;

}
