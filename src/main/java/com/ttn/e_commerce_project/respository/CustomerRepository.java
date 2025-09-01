package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.dto.vo.CustomerVo;
import com.ttn.e_commerce_project.dto.vo.SellerFlatVo;
import com.ttn.e_commerce_project.dto.vo.SellerVo;
import com.ttn.e_commerce_project.entity.user.Customer;
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
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    @Query("""
       SELECT new com.ttn.e_commerce_project.dto.vo.CustomerVo(
           u.id,
           CONCAT(u.firstName, ' ', COALESCE(u.middleName, ''), ' ', u.lastName),
           u.email,
           u.isActive
       )
       FROM Customer c
       JOIN c.user u
       WHERE (:email IS NULL OR u.email = :email)
       """)
  Page<CustomerVo> findAllCustomers(@Param("email") String email, Pageable pageable);

    @Modifying
    @Transactional
    @Query("""
    UPDATE User u
    SET u.isActive = true
    WHERE u.id = (
        SELECT c.user.id FROM Customer c WHERE c.id = :customerId
    )
    AND u.isActive = false
""")
    int activateCustomerIfNotActive(@Param("customerId") Long customerId);

  @Modifying
  @Transactional
  @Query("""
    UPDATE User u
    SET u.isActive = false
    WHERE u.id = (
        SELECT c.user.id FROM Customer c WHERE c.id = :customerId
    )
    AND u.isActive = true
""")
  int deactivateCustomerIfActive(@Param("customerId") Long customerId);

  Optional<Customer> findByUserEmail(String email);
}
