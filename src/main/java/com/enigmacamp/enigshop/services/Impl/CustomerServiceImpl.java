package com.enigmacamp.enigshop.services.Impl;

import com.enigmacamp.enigshop.models.dto.request.RegistrationRequest;
import com.enigmacamp.enigshop.models.dto.request.CustomerUpdateRequest;
import com.enigmacamp.enigshop.models.dto.request.MetaDataRequest;
import com.enigmacamp.enigshop.models.dto.response.CustomerResponse;
import com.enigmacamp.enigshop.models.entities.Customer;
import com.enigmacamp.enigshop.exceptions.RequestValidationException;
import com.enigmacamp.enigshop.exceptions.ResourceNotFoundException;
import com.enigmacamp.enigshop.models.entities.Image;
import com.enigmacamp.enigshop.models.entities.UserAccount;
import com.enigmacamp.enigshop.repositories.CustomerRepository;
import com.enigmacamp.enigshop.services.CustomerService;
import com.enigmacamp.enigshop.services.ImageService;
import com.enigmacamp.enigshop.services.UserService;
import com.enigmacamp.enigshop.utils.specifications.CustomerSpecification;
import com.enigmacamp.enigshop.utils.ConvertType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ImageService imageService;
    private final UserService userService;

    @Override
    public CustomerResponse createCustomer(RegistrationRequest request) {
        UserAccount userAccount = userService.getUserByUsername(request.username());

        Customer customer = Customer.builder()
                .fullName(request.fullName())
                .phoneNumber(request.phoneNumber())
                .address(request.address())
                .birthDate(request.birthDate())
                .userAccount(userAccount)
                .build();

        customerRepository.save(customer);
        return ConvertType.customerToCustomerResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(String customerId, CustomerUpdateRequest request, MultipartFile image) {
        Customer customer = findByIdOrThrow(customerId);

        boolean changes = false;

        if(request != null && request.fullName() != null && !request.fullName().equals(customer.getFullName())){
            customer.setFullName(request.fullName());
            changes = true;
        }
        if(request != null && request.phoneNumber() != null && !request.phoneNumber().equals(customer.getPhoneNumber())){
            customer.setPhoneNumber(request.phoneNumber());
            changes = true;
        }
        if(request != null && request.address() != null && !request.address().equals(customer.getAddress())){
            customer.setAddress(request.address());
            changes = true;
        }
        if(request != null && request.birthDate() != null && !request.birthDate().equals(customer.getBirthDate())){
            customer.setBirthDate(request.birthDate());
            changes = true;
        }
        if (image != null){
            Image savedImage = imageService.saveImage(image, "customer");
            customer.setImage(savedImage);
            changes = true;
        }

        if (!changes){
            throw new RequestValidationException("no data changes found");
        }

        customerRepository.save(customer);
        return ConvertType.customerToCustomerResponse(customer);
    }

    @Override
    public void deleteCustomer(String customerId) {
        Customer customer = findByIdOrThrow(customerId);
        customerRepository.delete(customer);
    }

    @Override
    public CustomerResponse getCustomerResponseById(String customerId) {
        Customer customer = findByIdOrThrow(customerId);
        return ConvertType.customerToCustomerResponse(customer);
    }

    private Customer findByIdOrThrow(String customerId){
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id %s not found".formatted(customerId)));
    }

    @Override
    public Page<CustomerResponse> getCustomers(MetaDataRequest request) {
        if (request.page() < 1) {
            throw new RequestValidationException("page must be greater than 0");
        }
        if (request.size() < 1) {
            throw new RequestValidationException("size must be greater than 0");
        }

        Specification<Customer> spec = CustomerSpecification.searchSpecification(request.query());
        Pageable pageable = PageRequest.of(request.page() - 1, request.size());

        Page<Customer> customers = customerRepository.findAll(spec, pageable);

        if (customers.isEmpty()){
            throw new ResourceNotFoundException("Customer not found");
        }

        return customers.map(ConvertType::customerToCustomerResponse);
    }

    @Override
    public List<CustomerResponse> searchCustomer(String name) {
        List<Customer> result = customerRepository.findByFullNameContainingIgnoreCase(name);
        if (result.isEmpty()){
            throw new ResourceNotFoundException("Customer with name %s not found".formatted(name));
        }
        return result.stream()
                .map(ConvertType::customerToCustomerResponse)
                .toList();
    }

    @Override
    public Customer getCustomerById(String customerId) {
        return findByIdOrThrow(customerId);
    }
}
