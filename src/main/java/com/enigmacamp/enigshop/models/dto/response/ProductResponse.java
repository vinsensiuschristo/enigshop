package com.enigmacamp.enigshop.models.dto.response;

public record ProductResponse(
        String id,
        String name,
        String description,
        Long price,
        Integer stock
) {}
