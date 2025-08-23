package com.ttn.e_commerce_project.entity.user;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long roleId;

  @Enumerated(value = EnumType.STRING)
  RoleAuthority authority;
}
