package com.enigmacamp.enigshop.utils.specifications;

import org.springframework.data.jpa.domain.Specification;

public class GlobalSpecification {
    public static <T> Specification<T> likeIgnoreCase(String nameColumn, String keyword) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get(nameColumn)), "%" + keyword.toLowerCase() + "%");
    }
}
