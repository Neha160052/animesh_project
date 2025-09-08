package com.ttn.e_commerce_project.util;

import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.CategoryRepository;
import com.ttn.e_commerce_project.respository.SellerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.ttn.e_commerce_project.constants.UserConstants.CATEGORY_NOT_FOUND;
import static com.ttn.e_commerce_project.constants.UserConstants.SELLER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ValidationSpecification {

    CategoryRepository categoryRepo;
    SellerRepository sellerRepo;

    public void validateQuery(String query) {
        if (!StringUtils.hasText(query)) {
            return;
        }
        String[] criteria = query.split(",");
        for (String criterion : criteria) {
            String[] parts = criterion.split(":");
            if (parts.length != 2) {
                throw new InvalidArgumentException("Invalid filter format. Use key:value, e.g., 'brand:Apple'.");
            }
            String key = parts[0].trim();
            String value = parts[1].trim();
            if (!StringUtils.hasText(value)) {
                throw new InvalidArgumentException("Search value for '" + key + "' cannot be empty.");
            }
            try {
                switch (key) {
                    case "categoryId":
                        long catId = Long.parseLong(value);
                        if (!categoryRepo.existsById(catId)) {
                            throw new ResourceNotFoundException(CATEGORY_NOT_FOUND + catId);
                        }
                        break;

                    case "sellerId":
                        long sellerId = Long.parseLong(value);
                        if (!sellerRepo.existsById(sellerId)) {
                            throw new ResourceNotFoundException(SELLER_NOT_FOUND + sellerId);
                        }
                        break;
                    case "brand","name":
                        break;
                    default:
                        throw new InvalidArgumentException("Invalid filter key: '" + key + "'.");
                }
            } catch (NumberFormatException e) {
                throw new InvalidArgumentException("Invalid ID format for '" + key + "'. Expected a number, but got '" + value + "'.");
            }
        }
    }}
