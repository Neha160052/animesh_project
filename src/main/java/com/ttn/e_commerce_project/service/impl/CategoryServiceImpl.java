package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.CategoryCo;
import com.ttn.e_commerce_project.dto.co.MetadataFieldCo;
import com.ttn.e_commerce_project.dto.vo.CategoryVo;
import com.ttn.e_commerce_project.dto.vo.ListCategoryVo;
import com.ttn.e_commerce_project.dto.vo.MetadataFieldVo;
import com.ttn.e_commerce_project.entity.category.Category;
import com.ttn.e_commerce_project.entity.category.CategoryMetaDataField;
import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.CategoryMetadataFieldRepository;
import com.ttn.e_commerce_project.respository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ttn.e_commerce_project.constants.UserConstants.FIELD_NAME_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    CategoryMetadataFieldRepository categoryMetadataRepo;
    CategoryRepository categoryRepo;

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
        Category parent =null;
        if(categoryCo.getParentId()!=null){
            parent = categoryRepo.findById(categoryCo.getParentId()).orElseThrow(()->new ResourceNotFoundException("parent category not found"));
        }
        // 1. root category unique
        if(parent ==null)
        {
            if(categoryRepo.existsByNameAndParentIsNull(name))
            {
                throw new InvalidArgumentException("Root category "+name+" already exists");
            }
        }else {
            // tree wide uniqueness
            Category root = findRoot(parent);
            if(categoryRepo.existsByNameAndParent(name, parent)){
                throw new InvalidArgumentException("category name \t"+name+" \t already exists in tree root \t "+root.getName());
            }
        }
        Category category = new Category();
        category.setName(name);
        category.setParent(parent);
        Category saved = categoryRepo.save(category);
        return new CategoryVo(saved.getId(), saved.getName(),"Category created successfully");
    }


    private Category findRoot(Category category) {
        return category.getParent()==null?category:findRoot(category.getParent());
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

}
