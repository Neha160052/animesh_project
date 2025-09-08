package com.ttn.e_commerce_project.dto.vo;

import java.util.List;

public record SellerListCategoryVo(Long id, String name,
                                   List<CategoryMetadataFieldVo> metadataFields,
                                   List<CategoryParentVo> parentChain) {}
