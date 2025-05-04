package com.enigmacamp.enigshop.models.dto.response;

public record ErrorResponse(
        String error,
        String message
) {}
