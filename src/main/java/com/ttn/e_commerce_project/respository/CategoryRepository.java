package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CategoryRepository extends JpaRepository<Category,Long> {

    boolean existsByNameAndParentIsNull(String name);
    boolean existsByNameAndParent(String name,Category parent);

}
