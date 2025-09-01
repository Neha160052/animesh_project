package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query(value = "SELECT * FROM address WHERE id = :addressId AND user_id = :userId", nativeQuery = true)
    Optional<Address> findByIdAndUserId(@Param("addressId") Long id,
                                        @Param("userId") Long userId);

    @Query(value = "SELECT * FROM address a WHERE a.user_id = :userId", nativeQuery = true)
    List<Address> findByUserId(@Param("userId") Long userId);
}
