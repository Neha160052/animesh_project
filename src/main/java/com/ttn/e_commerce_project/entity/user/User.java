package com.ttn.e_commerce_project.entity.user;
import com.ttn.e_commerce_project.entity.audit.Auditable;
import com.ttn.e_commerce_project.entity.address.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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
     ZonedDateTime passwordUpdateDate;

   @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
     Seller seller;

   @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
     Customer customer;

   @ManyToMany(mappedBy = "users")
     Set<Role> role;

   @OneToMany
   @JoinColumn(name = "user_id", referencedColumnName = "id") //when not given reference column name it was creating extra table
     List<Address> address;
}
