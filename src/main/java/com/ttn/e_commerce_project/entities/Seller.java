package com.ttn.e_commerce_project.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Seller
{
    @Id
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String gst;
    private Long companyContact;
    private String companyName;

    @OneToMany
    @JoinColumn(name = "seller_user_id",referencedColumnName = "user_id")
    private List<Product> product;
}
