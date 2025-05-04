package com.enigmacamp.enigshop.repositories;

import com.enigmacamp.enigshop.models.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {
    List<Customer> findByFullNameContainingIgnoreCase(String name);
}
