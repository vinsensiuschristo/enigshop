package com.enigmacamp.enigshop.services;

import com.enigmacamp.enigshop.models.entities.Customer;
import com.enigmacamp.enigshop.models.entities.UserAccount;
import com.enigmacamp.enigshop.models.enums.UserRoles;
import com.enigmacamp.enigshop.repositories.CustomerRepository;
import com.enigmacamp.enigshop.services.Impl.CustomerServiceImpl;
import com.enigmacamp.enigshop.utils.ConvertType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerServiceImpl underTest;

    @Mock
    UserService userService;

    @Test
    @DisplayName("Create Customer Success ")
    void canCreateCustomer() {
        //Average
        String username = "test";
        UserAccount userAccount = UserAccount.builder()
                .id("id-1")
                .username(username)
                .email("test@test.com")
                .isActive(true)
                .roles(ConvertType.userRolesToRole(List.of(UserRoles.CUSTOMER,UserRoles.ADMIN)))
                .build();

        when(userService.getUserByUsername(username)).thenReturn(null);

        LocalDateTime birthDate = LocalDateTime.parse("2000-10-10 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Customer customer = Customer.builder()
                .fullName("test")
                .userAccount(userAccount)
                .birthDate(LocalDate.from(birthDate))
                .address("test")
                .phoneNumber("test")
                .build();

        when(customerRepository.save(customer)).thenReturn(customer);

        //Act

    }

    @Test
    void updateCustomer() {
    }

    @Test
    void deleteCustomer() {
    }

    @Test
    void getCustomerResponseById() {
    }

    @Test
    void getCustomers() {
    }

    @Test
    void getCustomerById() {
    }
}