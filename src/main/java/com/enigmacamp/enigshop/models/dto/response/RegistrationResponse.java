package com.enigmacamp.enigshop.models.dto.response;

import com.enigmacamp.enigshop.models.enums.UserRoles;

import java.util.List;

public record RegistrationResponse(
        String userAccountId,
        String username,
        List<UserRoles> roles
) {
}
