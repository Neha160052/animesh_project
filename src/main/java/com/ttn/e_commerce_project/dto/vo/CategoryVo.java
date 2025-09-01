package com.ttn.e_commerce_project.dto.vo;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryVo {
    
     Long id;
     String name;
     String message;

    public CategoryVo(Long id, String name, String message) {
        this.id = id;
        this.name = name;
        this.message = message;
    }
}
