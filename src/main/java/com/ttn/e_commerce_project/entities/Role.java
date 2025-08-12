package com.ttn.e_commerce_project.entities;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long roleId;
  private String authority;

  @ManyToMany
  @JoinTable(name = "user_role",
          joinColumns = @JoinColumn(name ="user_id"),inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<User> users;
}
