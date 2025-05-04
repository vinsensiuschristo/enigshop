package com.enigmacamp.enigshop.services;

import com.enigmacamp.enigshop.models.dto.request.MetaDataRequest;
import com.enigmacamp.enigshop.models.dto.request.TransactionRequest;
import com.enigmacamp.enigshop.models.dto.response.TransactionResponse;
import com.enigmacamp.enigshop.models.entities.Transaction;
import org.springframework.data.domain.Page;

public interface TransactionService {
    TransactionResponse create(TransactionRequest transactionRequest);
    Page<TransactionResponse> getAll(MetaDataRequest metaDataRequest);
    Transaction getById(String transactionId);
    TransactionResponse getResponseById(String transactionId);
}
