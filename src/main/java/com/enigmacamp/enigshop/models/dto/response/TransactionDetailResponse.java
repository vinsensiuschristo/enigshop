package com.enigmacamp.enigshop.models.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionDetailResponse(
        String id,
        @JsonProperty("product")
        ProductResponse productResponse,
        @JsonProperty("product_price")
        Long productPrice,
        Integer quantity,
        Long subtotal
) {}