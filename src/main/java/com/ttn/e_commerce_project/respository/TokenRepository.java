package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.VerificationToken;
import com.ttn.e_commerce_project.entity.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken,Long> {

    Optional<VerificationToken> findByToken(String token);

    @Transactional
    @Modifying
    void deleteByUser(User user);
}
