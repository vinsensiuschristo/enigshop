package com.enigmacamp.enigshop.repositories;

import com.enigmacamp.enigshop.models.entities.Payment;
import com.enigmacamp.enigshop.models.entities.Transaction;
import com.enigmacamp.enigshop.models.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Payment findPaymentByTransaction(Transaction transaction);

    List<Payment> findPaymentsByTransactionStatus(TransactionStatus transactionStatus);
}