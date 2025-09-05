package com.ttn.e_commerce_project.util;

import com.ttn.e_commerce_project.entity.product.ProductVariation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ProductVariationSpecification {

    public static Specification<ProductVariation> filterByQuery(String query)
    {
        return (Root<ProductVariation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            // If the query is empty, don't add any predicate
            if (!StringUtils.hasText(query)) {
                return criteriaBuilder.conjunction(); // Represents an empty "where" clause
            }
            Predicate metadataMatch = criteriaBuilder.like(
                    criteriaBuilder.function("CAST", String.class, root.get("metadata")),
                    "%" + query.toLowerCase() + "%"
            );
            return metadataMatch;
        };
    }

    public static Specification<ProductVariation> belongsToProduct(Long productId) {
        return (Root<ProductVariation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("product").get("id"), productId);
    }
}
