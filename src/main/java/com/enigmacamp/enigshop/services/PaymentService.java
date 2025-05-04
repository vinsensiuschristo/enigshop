package com.enigmacamp.enigshop.services;

import com.enigmacamp.enigshop.models.entities.Payment;

public interface PaymentService {
    Payment createPayment(String transactionId);
}