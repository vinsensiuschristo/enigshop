package com.enigmacamp.enigshop.utils;

import com.enigmacamp.enigshop.models.dto.request.ProductCreateRequest;
import com.enigmacamp.enigshop.models.dto.request.UserAccountRegistrationRequest;
import com.enigmacamp.enigshop.models.dto.response.*;
import com.enigmacamp.enigshop.models.entities.*;
import com.enigmacamp.enigshop.models.enums.UserRoles;

import java.util.List;

public class ConvertType {

    public static ProductResponse productToProductResponse(Product product){
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock()
        );
    }

    public static CustomerResponse customerToCustomerResponse(Customer customer){
        return new CustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getBirthDate()
        );
    }

    public static DepartmentResponse departmentToDepartmentResponse(Department department){
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getCode(),
                department.getDescription()
        );
    }

    public static TransactionDetailResponse transactionDetailToTransactionDetailResponse(
            TransactionDetail transactionDetail){
        return new TransactionDetailResponse(
                transactionDetail.getId(),
                productToProductResponse(transactionDetail.getProduct()),
                transactionDetail.getProduct().getPrice(),
                transactionDetail.getQuantity(),
                transactionDetail.getPrice() * transactionDetail.getQuantity()
        );
    }

    public static UserAccountResponse userAccountToUserAccountResponse(UserAccount userAccount){
        return new UserAccountResponse(
                userAccount.getId(),
                roleToUserRoles(userAccount.getRoles())
        );
    }

    public static List<UserRoles> roleToUserRoles(List<Role> roles){
        return List.of(roles.stream().map(Role::getRole).toArray(UserRoles[]::new));
    }

    public static List<Role> userRolesToRole(List<UserRoles> userRoles){
        return List.of(userRoles.stream().map(role -> Role.builder().role(role).build()).toArray(Role[]::new));
    }

    public static UserAccount userAccountRequestToUserAccount(UserAccountRegistrationRequest request){
        return UserAccount.builder()
                .username(request.username())
                .password(request.password())
                .email(request.email())
                .isActive(request.isActive())
                .roles(request.roles())
                .build();
    }

    public static PaymentResponse paymentToPaymentResponse(Payment payment){
        return new PaymentResponse(
                payment.getToken(),
                payment.getRedirectUrl()
        );
    }
}
