package com.ttn.demo_project.e_commerce_project.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   private String city;
   private String state;
   private String country;
   private String addressLine;
   private int zipCode;
   public String label;
   @ManyToOne
   @JoinColumn(name = "user_id")
   private User user;
}
