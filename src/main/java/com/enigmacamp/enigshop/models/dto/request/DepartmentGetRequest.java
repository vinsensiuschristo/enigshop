package com.enigmacamp.enigshop.models.dto.request;

public record DepartmentGetRequest(
    String name,
    String code,
    String sortBy,
    String sortDirection,
    Integer page,
    Integer size
) {}
