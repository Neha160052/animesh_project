package com.ttn.e_commerce_project.util;

import com.ttn.e_commerce_project.entity.product.Product;
import com.ttn.e_commerce_project.entity.product.ProductVariation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;


public class ProductVariationSpecification {

    public static Specification<ProductVariation> isProductActive() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.join("product").get("isActive"));
    }

    public static Specification<ProductVariation> filterByQuery(String query)
    {
        return (Root<ProductVariation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            if (!StringUtils.hasText(query)) {
                return criteriaBuilder.conjunction();
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

    public static Specification<ProductVariation> belongsToCategory(Long categoryId) {
        return (Root<ProductVariation> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                cb.equal(root.get("product").get("category").get("id"), categoryId);
    }

}
