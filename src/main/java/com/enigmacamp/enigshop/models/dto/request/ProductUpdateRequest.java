package com.enigmacamp.enigshop.models.dto.request;

public record ProductUpdateRequest(
    String name,
    String description,
    Long price,
    Integer stock
) {}
