package com.ttn.e_commerce_project.dto.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
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
