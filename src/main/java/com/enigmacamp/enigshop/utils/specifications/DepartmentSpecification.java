package com.enigmacamp.enigshop.utils.specifications;

import com.enigmacamp.enigshop.constants.DepartmentColumn;
import com.enigmacamp.enigshop.models.entities.Department;
import org.springframework.data.jpa.domain.Specification;

public class DepartmentSpecification {

    public static Specification<Department> codeEquals(String code) {
        return (root,
                query,
                criteriaBuilder) -> criteriaBuilder.equal(root.get(DepartmentColumn.CODE.getColumn()), code);
    }

}
