package com.ttn.e_commerce_project.entity.user;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long roleId;
  String authority;

  @ManyToMany
  @JoinTable(name = "user_role",
          joinColumns = @JoinColumn(name ="user_id"),inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  Set<User> users;
}
