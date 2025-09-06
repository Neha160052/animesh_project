package com.ttn.e_commerce_project.respository;

import com.ttn.e_commerce_project.dto.vo.FilterStatsVo;

import java.util.Set;

public interface ProductRepositoryCustom {

    FilterStatsVo getFilterStatsForCategories(Set<Long> categoryIds);
}

