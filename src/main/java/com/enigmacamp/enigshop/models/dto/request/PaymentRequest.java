package com.enigmacamp.enigshop.models.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record PaymentRequest(
        @JsonProperty("transaction_details")
        PaymentDetailRequest paymentDetail,
        @JsonProperty("item_details")
        List<ItemDetailsRequest> itemDetails
) {
}
