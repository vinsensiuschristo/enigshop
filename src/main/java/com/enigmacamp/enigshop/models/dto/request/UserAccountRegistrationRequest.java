package com.enigmacamp.enigshop.models.dto.request;

import com.enigmacamp.enigshop.models.entities.Role;

import java.util.List;

public record UserAccountRegistrationRequest(
       String username,
       String password,
       String email,
       Boolean isActive,
       List<Role> roles
) {
}
