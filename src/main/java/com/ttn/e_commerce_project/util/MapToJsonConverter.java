package com.ttn.e_commerce_project.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.HashMap;
import java.util.Map;

@Converter
public class MapToJsonConverter implements AttributeConverter<Map<String,Object>,String> {

    final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> stringObjectMap) {
        try {
            return objectMapper.writeValueAsString(stringObjectMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error while serializing to map to json",e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error while converting to json to map",e);
        }
    }
}
