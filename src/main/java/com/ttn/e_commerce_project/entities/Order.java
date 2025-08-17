package com.ttn.e_commerce_project.entities;

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
public class Order extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    double amountPaid;
    ZonedDateTime dateCreated;

    @AttributeOverride(name = "city", column = @Column(name = "customer_address_city"))
    @AttributeOverride(name = "state", column = @Column(name = "customer_address_state"))
    @AttributeOverride(name = "country", column = @Column(name = "customer_address_country"))
    @AttributeOverride(name = "addressLine", column = @Column(name = "customer_address_address_line"))
    @AttributeOverride(name = "zipCode", column = @Column(name = "customer_address_zipcode"))
    @AttributeOverride(name = "label", column = @Column(name = "customer_address_label"))
    @Embedded
    AddressFields address;

    @OneToMany
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    Set<OrderProduct> orderProducts;

}
