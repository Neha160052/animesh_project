package com.ttn.e_commerce_project.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.security.Identity;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   private String firstName;
   private String middleName;
   private String lastName;
   private String password;
   private boolean isDeleted;
   private boolean isActive;
   private boolean isExpired;
   private boolean isLocked;
   private int invalidAttemptCount;
   private ZonedDateTime passwordUpdateDate;

   @OneToOne(mappedBy = "user")
   private Seller seller;

   @OneToOne(mappedBy = "user")
   private Customer customer;

   @ManyToMany(mappedBy = "users")
   private Set<Role> roles;

   @OneToMany
   @JoinColumn(name = "user_id", referencedColumnName = "id") //when not given reference column name it was creating extra table
   private List<Address> address;
}
