package com.ttn.e_commerce_project.dto.vo;

import java.util.List;

public record ListCategoryVo(

        Long id,
        String name,
        List<ParentInfo> parents,
        List<ChildInfo> children
)
{
    public record ParentInfo(Long id, String name) {}
    public record ChildInfo(Long id, String name) {}
}