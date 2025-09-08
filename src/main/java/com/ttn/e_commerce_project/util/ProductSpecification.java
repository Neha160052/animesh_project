package com.ttn.e_commerce_project.util;

import com.ttn.e_commerce_project.entity.product.Product;
import com.ttn.e_commerce_project.entity.user.Seller;
import com.ttn.e_commerce_project.respository.CategoryRepository;
import com.ttn.e_commerce_project.respository.SellerRepository;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class ProductSpecification {

    CategoryRepository categoryRepo;
    SellerRepository sellerRepo;

    public static Specification<Product> isActive() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isActive"));
    }

    public static Specification<Product> hasSeller(Seller seller) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("seller"), seller);
    }

    public static Specification<Product> filterByBrandOrName(String query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (!StringUtils.hasText(query)) {
                return criteriaBuilder.conjunction(); // No filter if query is empty
            }
            List<Predicate> predicates = new ArrayList<>();
            String[] criteria = query.split(",");

            for (String criterion : criteria) {
                String[] parts = criterion.split(":");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    String likePattern = "%" + value.toLowerCase() + "%";
                    switch (key) {
                        case "brand":
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), likePattern));
                            break;
                        case "name":
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern));
                            break;
                    }
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> filterByCriteria(String query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (!StringUtils.hasText(query)) {
                return criteriaBuilder.conjunction(); // No filter if query is empty
            }

            List<Predicate> predicates = new ArrayList<>();
            String[] criteria = query.split(",");

            for (String criterion : criteria) {
                String[] parts = criterion.split(":");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    switch (key) {
                        case "categoryId":
                            predicates.add(criteriaBuilder.equal(root.get("category").get("id"), Long.parseLong(value)));
                            break;
                        case "sellerId":
                            predicates.add(criteriaBuilder.equal(root.get("seller").get("id"), Long.parseLong(value)));
                            break;
                        case "brand":
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), "%" + value.toLowerCase() + "%"));
                            break;
                        case "name":
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + value.toLowerCase() + "%"));
                            break;
                    }
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> isSimilarTo(Product sourceProduct) {
        return (root, query, criteriaBuilder) -> {

            assert query != null;
            query.distinct(true);
            root.fetch("productVariation", JoinType.LEFT);

            Predicate categoryMatch = criteriaBuilder.equal(
                    root.get("category").get("id"),
                    sourceProduct.getCategory().getId()
            );

            Predicate notItself = criteriaBuilder.notEqual(
                    root.get("id"),
                    sourceProduct.getId()
            );

            return criteriaBuilder.and(categoryMatch, notItself);
        };
    }}
