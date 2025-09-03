package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.category.CategoryMetaDataValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryMetadataFieldValueRepo extends JpaRepository<CategoryMetaDataValues,Long> {

    List<CategoryMetaDataValues> findByCategoryIdAndCategoryMetaDataFieldId(Long categoryId, Long fieldId);

    List<CategoryMetaDataValues> findByCategoryId(long id);
}
