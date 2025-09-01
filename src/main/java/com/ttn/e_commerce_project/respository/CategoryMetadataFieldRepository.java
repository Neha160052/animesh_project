package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.category.CategoryMetaDataField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryMetadataFieldRepository extends JpaRepository<CategoryMetaDataField,Long> {

    Optional<CategoryMetaDataField> findByNameIgnoreCase(String name);
    Page<CategoryMetaDataField> findByNameContainingIgnoreCase(String query, Pageable pageable);
}

