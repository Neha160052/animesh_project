package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.dto.vo.SellerFlatVo;
import com.ttn.e_commerce_project.entity.user.Seller;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {

    boolean existsByGst(String gst);

    boolean existsByCompanyNameIgnoreCase(String companyName);

    Optional<Seller> findByUserEmail(String email);

    @Query("""
       SELECT new com.ttn.e_commerce_project.dto.vo.SellerFlatVo(
           u.id,
           CONCAT(u.firstName, ' ', COALESCE(u.middleName, ''), ' ', u.lastName),
           u.email,
           u.isActive,
           s.companyName,
           s.companyContact,
           a.city,
           a.state,
           a.country,
           a.addressLine,
           a.zipCode,
           a.label
       )
       FROM Seller s
       JOIN s.user u
       JOIN u.address a
       WHERE (:email IS NULL OR u.email = :email)
       """)
    Page<SellerFlatVo> findAllSellers(@Param("email")String email, Pageable pageable);

    @Modifying
    @Transactional
    @Query("""
    UPDATE User u
    SET u.isActive = true
    WHERE u.id = (
        SELECT c.user.id FROM Seller c WHERE c.id = :sellerId
    )
    AND u.isActive = false
""")
    int activateSellerIfNotActive(@Param("sellerId") Long sellerId);

    @Modifying
    @Transactional
    @Query("""
    UPDATE User u
    SET u.isActive = false
    WHERE u.id = (
        SELECT s.user.id FROM Seller s WHERE s.id = :sellerId
    )
    AND u.isActive = true
""")
    int deactivateSellerIfActive(@Param("sellerId") Long sellerId);
}
