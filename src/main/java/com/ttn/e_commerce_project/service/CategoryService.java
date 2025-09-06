package com.ttn.e_commerce_project.service;

import com.ttn.e_commerce_project.dto.co.CategoryCo;
import com.ttn.e_commerce_project.dto.co.CategoryMetaDataCo;
import com.ttn.e_commerce_project.dto.co.CategoryMetaDataUpdateCo;
import com.ttn.e_commerce_project.dto.co.MetadataFieldCo;
import com.ttn.e_commerce_project.dto.vo.*;
import com.ttn.e_commerce_project.entity.category.Category;
import com.ttn.e_commerce_project.entity.category.CategoryMetaDataField;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    CategoryMetaDataField addMetaDataField(MetadataFieldCo metadataFieldCo);
    Page<MetadataFieldVo> getAllMetadataFields(int offset, int max, String sortBy, String order, String query);
    CategoryVo addCategory(CategoryCo categoryCo);
    ListCategoryVo getCategoryById(Long id);
    Page<ListCategoryVo> getAllCategories(int max, int offset, String sort, String order, String query);
    ResponseEntity<String> updateCategory(Long id, CategoryCo categoryCo);
    ResponseEntity<String> addMetadata(CategoryMetaDataCo metaDataCo);
    void updateMetadataValues(@Valid CategoryMetaDataUpdateCo metaDataUpdateCo);
    List<SellerListCategoryVo> getAllLeafCategories();
    List<Category> getCategories(Long categoryId);
    FilterCategoryVo getFilterForCategory(Long categoryId);
}
