package com.ttn.e_commerce_project.entity.user;

import com.ttn.e_commerce_project.entity.address.Address;
import com.ttn.e_commerce_project.entity.audit.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class User extends Auditable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
     long id;
     String email;
     String firstName;
     String middleName;
     String lastName;
     String password;
     boolean isDeleted;
     boolean isActive;
     boolean isExpired;
     boolean isLocked;
     int invalidAttemptCount;
     LocalDateTime lockTime;
     ZonedDateTime passwordUpdateDate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
     Seller seller;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
     Customer customer;

     @ManyToMany
     @JoinTable(name = "user_role",
             joinColumns = @JoinColumn(name ="user_id"),inverseJoinColumns = @JoinColumn(name = "role_id")
     )
     Set<Role> role;

   @OneToMany(cascade = CascadeType.ALL)
   @JoinColumn(name = "user_id", referencedColumnName = "id")
   List<Address> address;
}
