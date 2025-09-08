package com.ttn.e_commerce_project.dto.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryWithChildrenVo {

    Long id;
    String name;
    List<CategoryVo> children;
}
