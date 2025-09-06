package com.ttn.e_commerce_project.dto.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterCategoryVo {

    List<MetadataField> metadata;
    List<String> brands;
    PriceRangeVo priceRange;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class MetadataField {
        private String name;
        private List<String> values;
    }
}
