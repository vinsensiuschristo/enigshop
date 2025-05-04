package com.enigmacamp.enigshop.models.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public record TransactionResponse(
        String id,
        CustomerResponse customer,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime date,
        @JsonProperty("transaction_details")
        List<TransactionDetailResponse> transactionDetailResponses,
        @JsonProperty("total_payment")
        Long totalPayment
) {}