package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category,Long> {

    boolean existsByNameAndParentIsNull(String name);
    boolean existsByNameAndParent(String name,Category parent);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.parent WHERE c.id = :id")
    Optional<Category> findByIdWithParent(@Param("id") Long id);
    List<Category> findByParentId(Long parentId);
    Page<Category> findByNameContainingIgnoreCase(String query, Pageable pageable);

    boolean existsByNameAndParentId(String name , Long parentId);
}
