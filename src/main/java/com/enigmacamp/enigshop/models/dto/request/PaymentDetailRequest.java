package com.enigmacamp.enigshop.models.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record PaymentDetailRequest(
        @JsonProperty("order_id")
        String orderId,
        @JsonProperty("gross_amount")
        Long grossAmount
) {
}
