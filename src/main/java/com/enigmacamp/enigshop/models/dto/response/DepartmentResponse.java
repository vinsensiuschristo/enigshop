package com.enigmacamp.enigshop.models.dto.response;

public record DepartmentResponse(
        Long id,
        String name,
        String code,
        String description
) {}
