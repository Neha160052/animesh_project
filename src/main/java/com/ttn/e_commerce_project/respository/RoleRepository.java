package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
     Set<Role> findByAuthority(String authority);
}
