package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
     Optional<Role> findByAuthority(RoleAuthority authority);
}
