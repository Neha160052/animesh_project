package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.token.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist,Long> {

    boolean existsByJti(String jti);
}
