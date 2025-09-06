package com.ttn.e_commerce_project.respository.impl;

import com.ttn.e_commerce_project.dto.vo.FilterStatsVo;
import com.ttn.e_commerce_project.dto.vo.PriceRangeVo;
import com.ttn.e_commerce_project.entity.product.Product;
import com.ttn.e_commerce_project.entity.product.ProductVariation;
import com.ttn.e_commerce_project.respository.ProductRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    EntityManager entityManager;

    @Override
    public FilterStatsVo getFilterStatsForCategories(Set<Long> categoryIds) {
        if(categoryIds==null || categoryIds.isEmpty())
            return new FilterStatsVo(Collections.emptyList(),new PriceRangeVo(0.0,0.0));

        List<String> brands = getDistinctBrands(categoryIds);
        PriceRangeVo priceRange = getPriceRange(categoryIds);
        return new FilterStatsVo(brands, priceRange);
    }

    private List<String> getDistinctBrands(Set<Long> categoryIds)
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<Product> product = query.from(Product.class);

        query.select(product.get("brand")).distinct(true);
        Predicate categoryIdPredicate = product.get("categoryId").get("id").in(categoryIds);
        query.where(categoryIdPredicate);
        query.orderBy(cb.asc(product.get("brand")));

        return entityManager.createQuery(query).getResultList();
    }

    private PriceRangeVo getPriceRange(Set<Long> categoryIds){

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Product> product = query.from(Product.class);

        Join<Product, ProductVariation> variations = product.join("productVariation");
        query.multiselect(
                cb.min(variations.get("price")).alias("minPrice"),
                cb.max(variations.get("price")).alias("maxPrice")
        );

        Predicate categoryIdPredicate = product.get("category").get("id").in(categoryIds);
        query.where(categoryIdPredicate);
        List<Tuple> results = entityManager.createQuery(query).getResultList();
        if (results.isEmpty() || results.get(0).get("minPrice") == null) {
            return new PriceRangeVo(0.0, 0.0);
        }
        Tuple result = results.get(0);
        Double minPrice = result.get("minPrice", Double.class);
        Double maxPrice = result.get("maxPrice", Double.class);
        return new PriceRangeVo(minPrice, maxPrice);
    }

}
