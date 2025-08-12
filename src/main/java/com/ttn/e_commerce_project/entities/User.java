package com.ttn.e_commerce_project.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private String firstName;
   private String middleName;
   private String lastName;
   private String password;
   private boolean isDeleted;
   private boolean isActive;
   private boolean isExpired;
   private boolean isLocked;
   private int invalidAttemptCount;
   private Date passwordUpdateDate;

   @OneToOne(mappedBy = "user")
   private Seller seller;

   @OneToOne(mappedBy = "user")
   private Customer customer;

   @ManyToMany(mappedBy = "users")
   private Set<Role> roles;

   @OneToMany
   @JoinColumn(name = "user_id")
   private List<Address> address;
}
