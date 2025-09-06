package com.ttn.e_commerce_project.dto.vo;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterStatsVo {
    List<String> brands;
    PriceRangeVo priceRange;
}
