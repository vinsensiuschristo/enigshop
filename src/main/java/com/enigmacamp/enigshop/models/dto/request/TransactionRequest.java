package com.enigmacamp.enigshop.models.dto.request;

import java.util.List;

public record TransactionRequest(
        String customerId,
        List<TransactionDetailRequest> transactionDetailsRequest
) {}