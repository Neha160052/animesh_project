package com.ttn.e_commerce_project.entities;
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
public class Seller extends Auditable
{
    @Id
    long userid;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    User user;
    String gst;
    Long companyContact;
    String companyName;

    @OneToMany
    @JoinColumn(name = "seller_user_id",referencedColumnName = "user_id")
    List<Product> product;
}
