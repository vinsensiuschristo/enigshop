package com.enigmacamp.enigshop.models.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ItemDetailsRequest(
        @JsonProperty("id")
        String productId,
        Long price,
        Integer quantity,
        String name
) {
}
