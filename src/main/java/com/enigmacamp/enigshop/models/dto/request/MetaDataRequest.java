package com.enigmacamp.enigshop.models.dto.request;

public record MetaDataRequest(
        String query,
        Integer page,
        Integer size
) {}
