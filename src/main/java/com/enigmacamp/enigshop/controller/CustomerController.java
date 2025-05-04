package com.enigmacamp.enigshop.controller;

import com.enigmacamp.enigshop.constants.APIUrl;
import com.enigmacamp.enigshop.models.dto.request.RegistrationRequest;
import com.enigmacamp.enigshop.models.dto.request.CustomerUpdateRequest;
import com.enigmacamp.enigshop.models.dto.request.MetaDataRequest;
import com.enigmacamp.enigshop.models.dto.response.*;
import com.enigmacamp.enigshop.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = APIUrl.CUSTOMER_API)
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final ObjectMapper objectMapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getCustomers(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ){
        MetaDataRequest dataRequest = new MetaDataRequest(search, page, size);
        Page<CustomerResponse> customers = customerService.getCustomers(dataRequest);

        long totalData = customers.getTotalElements();
        long totalPage = totalData % size == 0 ? totalData / size : totalData / size + 1;
        boolean hasNext = customers.hasNext();
        boolean hasPrevious = customers.hasPrevious();

        MetaDataResponse metaData = new MetaDataResponse(page, size, totalData, totalPage, hasNext, hasPrevious);
        return ResponseEntity
                .ok(new CommonResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        customers.getContent(),
                        metaData
                ));
    }

    @GetMapping("{id}")
    public ResponseEntity<CommonResponse<CustomerResponse>> getCustomerById(@PathVariable String id){
        return ResponseEntity
                .ok(new CommonResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        customerService.getCustomerResponseById(id),
                        null
                ));
    }

    @PostMapping
    public ResponseEntity<CommonResponse<CustomerResponse>> addCustomer(@RequestBody RegistrationRequest request) {
        return ResponseEntity
                .ok(new CommonResponse<>(
                        HttpStatus.CREATED.value(),
                        "Customer created",
                        customerService.createCustomer(request),
                        null
                ));
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<CustomerDeleteResponse> deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity
                .ok(new CustomerDeleteResponse(id, "Customer deleted"));
    }

    @PutMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomer(
            @PathVariable("id") String id,
            @RequestPart(value = "customer", required = false) String customerJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {

        CustomerUpdateRequest customerUpdateRequest = null;

        if (customerJson != null) {
            try {
                customerUpdateRequest = objectMapper.readValue(customerJson, CustomerUpdateRequest.class);
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(new CommonResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid JSON format for customer data",
                        null,
                        null
                ));
            }
        }

        CustomerResponse customerResponse = customerService.updateCustomer(id, customerUpdateRequest, image);

        return ResponseEntity.ok(new CommonResponse<>(
                HttpStatus.OK.value(),
                "Customer updated successfully",
                customerResponse,
                null
        ));
    }
}
