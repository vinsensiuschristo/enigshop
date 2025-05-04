package com.enigmacamp.enigshop.utils.specifications;

import com.enigmacamp.enigshop.constants.CustomerColumn;
import com.enigmacamp.enigshop.exceptions.RequestValidationException;
import com.enigmacamp.enigshop.models.entities.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {

    public static Specification<Customer> searchSpecification(String search) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(search)) return criteriaBuilder.conjunction();

            List<Predicate> predicates = new ArrayList<>();

            if (!CustomerColumn.isValidColumn(search)){
                throw new RequestValidationException("Invalid column name");
            }else{
                for (CustomerColumn column : CustomerColumn.values()) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(column.getColumn())), "%" + search.toLowerCase() + "%"));
                }
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));

        };
    }
}
