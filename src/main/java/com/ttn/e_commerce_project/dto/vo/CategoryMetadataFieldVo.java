package com.ttn.e_commerce_project.dto.vo;

import java.util.List;

public record CategoryMetadataFieldVo(Long id, String fieldName, List<String> fieldValues) {
}
