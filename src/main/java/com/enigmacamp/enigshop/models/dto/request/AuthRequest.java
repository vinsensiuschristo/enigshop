package com.enigmacamp.enigshop.models.dto.request;

public record AuthRequest(
        String username,
        String password
) { }
