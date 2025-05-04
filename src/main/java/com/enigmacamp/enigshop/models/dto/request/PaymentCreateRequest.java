package com.enigmacamp.enigshop.models.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentCreateRequest(
        @JsonProperty("transaction_id")
        String transactionId
) {
}
