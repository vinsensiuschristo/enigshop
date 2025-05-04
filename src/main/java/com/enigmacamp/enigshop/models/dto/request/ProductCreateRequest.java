package com.enigmacamp.enigshop.models.dto.request;

public record ProductCreateRequest(
        String name,
        String description,
        Long price,
        Integer stock
){}
