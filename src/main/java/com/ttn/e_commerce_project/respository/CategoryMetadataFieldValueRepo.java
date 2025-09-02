package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.category.CategoryMetaDataValues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMetadataFieldValueRepo extends JpaRepository<CategoryMetaDataValues,Long> {

}
