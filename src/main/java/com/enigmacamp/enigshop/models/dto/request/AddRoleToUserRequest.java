package com.enigmacamp.enigshop.models.dto.request;

public record AddRoleToUserRequest(
        String userId,
        String role
) { }
