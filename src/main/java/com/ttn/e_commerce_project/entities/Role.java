package com.ttn.demo_project.e_commerce_project.entities;

import jakarta.persistence.*;

@Entity
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int roleId;
  private String authority;

  @ManyToMany
  @JoinTable(name = "user_role",
          joinColumns = @JoinColumn(name ="user_id"),inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private User user;
}
