package com.enigmacamp.enigshop.services;

import com.enigmacamp.enigshop.models.dto.request.RegistrationRequest;
import com.enigmacamp.enigshop.models.dto.request.CustomerUpdateRequest;
import com.enigmacamp.enigshop.models.dto.request.MetaDataRequest;
import com.enigmacamp.enigshop.models.dto.response.CustomerResponse;
import com.enigmacamp.enigshop.models.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(RegistrationRequest request);
    CustomerResponse updateCustomer(String customerId, CustomerUpdateRequest request, MultipartFile image);
    void deleteCustomer(String customerId);
    CustomerResponse getCustomerResponseById(String customerId);
    Page<CustomerResponse> getCustomers(MetaDataRequest request);
    List<CustomerResponse> searchCustomer(String name);
    Customer getCustomerById(String customerId);
}
