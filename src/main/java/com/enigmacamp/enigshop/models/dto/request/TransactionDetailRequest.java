package com.enigmacamp.enigshop.models.dto.request;

public record TransactionDetailRequest(
        String productId,
        Integer quantity
) {}