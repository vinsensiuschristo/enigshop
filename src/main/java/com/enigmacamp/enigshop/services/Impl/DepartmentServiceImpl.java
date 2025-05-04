package com.enigmacamp.enigshop.services.Impl;

import com.enigmacamp.enigshop.constants.DepartmentColumn;
import com.enigmacamp.enigshop.exceptions.RequestValidationException;
import com.enigmacamp.enigshop.exceptions.ResourceNotFoundException;
import com.enigmacamp.enigshop.models.dto.request.DepartmentGetRequest;
import com.enigmacamp.enigshop.models.dto.request.DepartmentRegistrationRequest;
import com.enigmacamp.enigshop.models.dto.response.DepartmentResponse;
import com.enigmacamp.enigshop.models.entities.Department;
import com.enigmacamp.enigshop.repositories.DepartmentRepository;
import com.enigmacamp.enigshop.services.DepartmentService;
import com.enigmacamp.enigshop.utils.specifications.DepartmentSpecification;
import com.enigmacamp.enigshop.utils.specifications.GlobalSpecification;
import com.enigmacamp.enigshop.utils.ConvertType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponse createDepartment(DepartmentRegistrationRequest request) {
        Department department = new Department(
                request.name(),
                request.code(),
                request.description()
        );
        departmentRepository.save(department);
        return ConvertType.departmentToDepartmentResponse(department);
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        Department department = findByIdOrThrow(departmentId);
        departmentRepository.delete(department);
    }

    @Override
    public DepartmentResponse getDepartmentById(Long departmentId) {
        Department department = findByIdOrThrow(departmentId);
        return ConvertType.departmentToDepartmentResponse(department);
    }

    private Department findByIdOrThrow(Long departmentId){
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department with id %s not found".formatted(departmentId)));
    }

    @Override
    public Page<DepartmentResponse> getDepartments(DepartmentGetRequest request) {
        if (request.page() < 1) {
            throw new RequestValidationException("page must be greater than 0");
        }
        if (request.size() < 1) {
            throw new RequestValidationException("size must be greater than 0");
        }

        Specification<Department> spec = buildSpecification(request);
        Pageable pageable = buildPageable(request);

        Page<Department> departments = spec == null
                ? departmentRepository.findAll(pageable)
                : departmentRepository.findAll(spec, pageable);

        if (departments.isEmpty()) {
            throw new ResourceNotFoundException("Department not found");
        }

        return departments.map(ConvertType::departmentToDepartmentResponse);
    }

    private Specification<Department> buildSpecification(DepartmentGetRequest request) {
        if (request.code() != null && !request.code().isEmpty()) {
            return Specification.where(DepartmentSpecification.codeEquals(request.code()));
        }

        if (request.name() != null && !request.name().isEmpty()) {
            return Specification.where(GlobalSpecification.likeIgnoreCase(DepartmentColumn.NAME.getColumn(), request.name()));
        }

        return null;
    }

    private Pageable buildPageable(DepartmentGetRequest request) {
        String sortBy = request.sortBy() != null
                && !request.sortBy().isEmpty() ? request.sortBy() : "id";

        if (!DepartmentColumn.isValidColumn(sortBy)) {
            throw new RequestValidationException("Invalid sortBy column");
        }

        String directionInput = request.sortDirection() != null
                && !request.sortDirection().isEmpty() ? request.sortDirection().toLowerCase() : "asc";

        if (!directionInput.equals("desc") && !directionInput.equals("asc")) {
            throw new RequestValidationException("Invalid sortDirection, must be 'asc' or 'desc'");
        }

        Sort.Direction direction = Sort.Direction.fromString(directionInput);

        return PageRequest.of(request.page() - 1, request.size(), Sort.by(direction, sortBy));
    }

    @Override
    public DepartmentResponse updateDepartment(Long departmentId, DepartmentRegistrationRequest request) {
        Department department = findByIdOrThrow(departmentId);
        boolean changes = false;

        if (request.name() != null && !request.name().isEmpty() && !request.name().equals(department.getName())) {
            department.setName(request.name());
            changes = true;
        }
        if (request.code() != null && !request.code().isEmpty() && !request.code().equals(department.getCode())) {
            department.setCode(request.code());
            changes = true;
        }
        if (request.description() != null && !request.description().isEmpty() && !request.description().equals(department.getDescription())) {
            department.setDescription(request.description());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No changes detected");
        }
        departmentRepository.save(department);
        return ConvertType.departmentToDepartmentResponse(department);
    }
}
