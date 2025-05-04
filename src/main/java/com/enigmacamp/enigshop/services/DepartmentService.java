package com.enigmacamp.enigshop.services;

import com.enigmacamp.enigshop.models.dto.request.DepartmentGetRequest;
import com.enigmacamp.enigshop.models.dto.request.DepartmentRegistrationRequest;
import com.enigmacamp.enigshop.models.dto.response.DepartmentResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentService {
    DepartmentResponse createDepartment(DepartmentRegistrationRequest request);
    void deleteDepartment(Long departmentId);
    DepartmentResponse getDepartmentById(Long departmentId);
    Page<DepartmentResponse> getDepartments(DepartmentGetRequest request);
    DepartmentResponse updateDepartment(Long departmentId, DepartmentRegistrationRequest request);
}
