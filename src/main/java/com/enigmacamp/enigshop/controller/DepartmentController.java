package com.enigmacamp.enigshop.controller;

import com.enigmacamp.enigshop.constants.APIUrl;
import com.enigmacamp.enigshop.models.dto.request.DepartmentGetRequest;
import com.enigmacamp.enigshop.models.dto.request.DepartmentRegistrationRequest;
import com.enigmacamp.enigshop.models.dto.response.CommonResponse;
import com.enigmacamp.enigshop.models.dto.response.DepartmentResponse;
import com.enigmacamp.enigshop.models.dto.response.MetaDataResponse;
import com.enigmacamp.enigshop.services.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.DEPARTMENT_API)
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<DepartmentResponse>>> getDepartments(
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "sortDirection", required = false) String sortDirection,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ){
        DepartmentGetRequest departmentGetRequest = new DepartmentGetRequest(
                name,
                code,
                sortBy,
                sortDirection,
                page,
                size
        );

        Page<DepartmentResponse> departments = departmentService.getDepartments(departmentGetRequest);
        long total = departments.getTotalElements();
        long totalPage = departments.getTotalPages();
        boolean hasNext = departments.hasNext();
        boolean hasPrevious = departments.hasPrevious();

        MetaDataResponse metaDataResponse = new MetaDataResponse(page, size, total, totalPage, hasNext, hasPrevious);

        return ResponseEntity
                .ok(new CommonResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        departments.getContent(),
                        metaDataResponse
                ));
    }


    @PostMapping
    public ResponseEntity<CommonResponse<DepartmentResponse>> addDepartment(DepartmentRegistrationRequest request){
        DepartmentResponse department = departmentService.createDepartment(request);

        return ResponseEntity
               .ok(new CommonResponse<>(
                       HttpStatus.CREATED.value(),
                   "Department Created",
                       department,
                       null
               ));
    }

    @PutMapping("{id}")
    public ResponseEntity<CommonResponse<DepartmentResponse>> updateDepartment(
            @PathVariable Long id, @RequestBody DepartmentRegistrationRequest request){
        DepartmentResponse department = departmentService.updateDepartment(id, request);

        return ResponseEntity
                .ok(new CommonResponse<>(
                        HttpStatus.OK.value(),
                        "Department Updated",
                        department,
                        null
                ));
    }

    @DeleteMapping("{id}")
    public void deleteDepartment(@PathVariable Long id){
        departmentService.deleteDepartment(id);
    }


}
