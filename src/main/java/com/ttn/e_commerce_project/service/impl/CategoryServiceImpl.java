package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.CategoryCo;
import com.ttn.e_commerce_project.dto.co.CategoryMetaDataCo;
import com.ttn.e_commerce_project.dto.co.MetadataFieldCo;
import com.ttn.e_commerce_project.dto.vo.CategoryVo;
import com.ttn.e_commerce_project.dto.vo.ListCategoryVo;
import com.ttn.e_commerce_project.dto.vo.MetadataFieldVo;
import com.ttn.e_commerce_project.entity.category.Category;
import com.ttn.e_commerce_project.entity.category.CategoryMetaDataField;
import com.ttn.e_commerce_project.entity.category.CategoryMetaDataValues;
import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.CategoryMetadataFieldRepository;
import com.ttn.e_commerce_project.respository.CategoryMetadataFieldValueRepo;
import com.ttn.e_commerce_project.respository.CategoryRepository;
import com.ttn.e_commerce_project.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ttn.e_commerce_project.constants.UserConstants.FIELD_NAME_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    CategoryMetadataFieldRepository categoryMetadataRepo;
    CategoryRepository categoryRepo;
    CategoryMetadataFieldValueRepo metadataFieldValueRepo;

    public CategoryMetaDataField addMetaDataField(MetadataFieldCo metadataFieldCo) {

        categoryMetadataRepo.findByNameIgnoreCase(metadataFieldCo.getName()).ifPresent(field -> {
            throw new InvalidArgumentException(FIELD_NAME_ALREADY_EXISTS);
        });

        CategoryMetaDataField field = new CategoryMetaDataField();
        field.setName(metadataFieldCo.getName());
        return categoryMetadataRepo.save(field);
    }

    public Page<MetadataFieldVo> getAllMetadataFields(int offset, int max, String sortBy, String order, String query) {
        Sort sort = order.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(offset, max, sort);
        Page<CategoryMetaDataField> result;
        if (query != null && !query.trim().isEmpty())
            result = categoryMetadataRepo.findByNameContainingIgnoreCase(query, pageable);
        else
            result = categoryMetadataRepo.findAll(pageable);
        return result.map(field -> new MetadataFieldVo(field.getId(), field.getName()));
    }


    public CategoryVo addCategory(CategoryCo categoryCo) {
        String name = categoryCo.getName();
        Category parent = null;
        if (categoryCo.getParentId() != null) {
            parent = categoryRepo.findById(categoryCo.getParentId()).orElseThrow(() -> new ResourceNotFoundException("parent category not found"));
        }
        // 1. root category unique
        if (parent == null) {
            if (categoryRepo.existsByNameAndParentIsNull(name)) {
                throw new InvalidArgumentException("Root category " + name + " already exists");
            }
        } else {
            // tree wide uniqueness
            Category root = findRoot(parent);
            if (categoryRepo.existsByNameAndParent(name, parent)) {
                throw new InvalidArgumentException("category name \t" + name + " \t already exists in tree root \t " + root.getName());
            }
        }
        Category category = new Category();
        category.setName(name);
        category.setParent(parent);
        category.setLeaf(true);

        Category saved = categoryRepo.save(category);

        if (parent != null && parent.isLeaf()) {
            parent.setLeaf(false);
            categoryRepo.save(parent);
        }
        return new CategoryVo(saved.getId(), saved.getName(), "Category created successfully");
    }


    private Category findRoot(Category category) {
        return category.getParent() == null ? category : findRoot(category.getParent());
    }

    public ListCategoryVo getCategoryById(Long id) {

        Category category = categoryRepo.findByIdWithParent(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        List<Category> childrenEntities = categoryRepo.findByParentId(id);

        List<ListCategoryVo.ParentInfo> parents = new ArrayList<>();
        Category parent = category.getParent();
        while (parent != null) {
            parents.add(new ListCategoryVo.ParentInfo(parent.getId(), parent.getName()));
            parent = parent.getParent();
        }
        Collections.reverse(parents); // so root appears first

        // build children
        List<ListCategoryVo.ChildInfo> children = childrenEntities.stream()
                .map(child -> new ListCategoryVo.ChildInfo(child.getId(), child.getName()))
                .toList();


        return new ListCategoryVo(
                category.getId(),
                category.getName(),
                parents,
                children
        );
    }

    public Page<ListCategoryVo> getAllCategories(int max, int offset, String sort, String order, String query) {

        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(offset, max, Sort.by(direction, sort));

        Page<Category> categoryPage;

        if (query != null && !query.isEmpty()) {
            categoryPage = categoryRepo.findByNameContainingIgnoreCase(query, pageable);
        } else {
            categoryPage = categoryRepo.findAll(pageable);
        }

        return categoryPage.map(this::mapToVo);
    }

    private ListCategoryVo mapToVo(Category category) {
        // Map parents up to root
        List<ListCategoryVo.ParentInfo> parents = new ArrayList<>();
        Category currentParent = category.getParent();
        while (currentParent != null) {
            parents.add(new ListCategoryVo.ParentInfo(currentParent.getId(), currentParent.getName()));
            currentParent = currentParent.getParent();
        }
        Collections.reverse(parents); // optional: to start from root

        // Map immediate children
        List<ListCategoryVo.ChildInfo> children = category.getChildren().stream()
                .map(child -> new ListCategoryVo.ChildInfo(child.getId(), child.getName()))
                .toList();

        return new ListCategoryVo(
                category.getId(),
                category.getName(),
                parents,
                children
        );
    }

    public ResponseEntity<String> updateCategory(Long id, CategoryCo categoryCo) {

        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category ID not found"));

        String newName = categoryCo.getName();

        // Check uniqueness based on parent
        if (category.getParent() == null) {
            // Root category case
            if (categoryRepo.existsByNameAndParentIsNull(newName)
                    && !category.getName().equalsIgnoreCase(newName)) {
                throw new InvalidArgumentException("Root category '" + newName + "' already exists");
            }
        } else {
            // Child category case
            Long parentId = category.getParent().getId();
            if (categoryRepo.existsByNameAndParentId(newName, parentId)
                    && !category.getName().equalsIgnoreCase(newName)) {
                throw new InvalidArgumentException("Category name '" + newName
                        + "' already exists under parent '" + category.getParent().getName() + "'");
            }
        }
        category.setName(newName);

        categoryRepo.save(category);

        return ResponseEntity.ok("Category updated successfully");
    }

    public ResponseEntity<String> addMetadata(CategoryMetaDataCo metaDataCo) {
        if (metaDataCo.getFieldValues().size() != metaDataCo.getFieldValues().stream().distinct().count()) {
            return ResponseEntity.badRequest().body("values must be unique within the list");
        }

        Category category = categoryRepo.findById(metaDataCo.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with the given id not found"));

        if (!category.isLeaf()) {
            throw new InvalidArgumentException(
                    "Metadata values can only be added to leaf categories. Category '" + category.getName() + "' is not a leaf.");
        }

            CategoryMetaDataField metaDataField = categoryMetadataRepo.findById(metaDataCo.getMetaDataFieldId())
                    .orElseThrow(() -> new ResourceNotFoundException("Metadata field with the given fieldId not found"));

            for (String value : metaDataCo.getFieldValues()) {
                CategoryMetaDataValues metaDataValues = new CategoryMetaDataValues();
                metaDataValues.setFieldValues(value);
                metaDataValues.setCategory(category);
                metaDataValues.setCategoryMetaDataField(metaDataField);
                metadataFieldValueRepo.save(metaDataValues);
            }

            return ResponseEntity.ok("metaDataField values added successfully");
        }
}
