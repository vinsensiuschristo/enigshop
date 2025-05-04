package com.enigmacamp.enigshop.repositories;

import com.enigmacamp.enigshop.models.entities.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, String> {
    List<TransactionDetail> findTransactionDetailsByTransaction_Id(String id);
}
