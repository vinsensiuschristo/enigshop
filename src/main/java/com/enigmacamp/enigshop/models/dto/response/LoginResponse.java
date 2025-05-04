package com.enigmacamp.enigshop.models.dto.response;

import com.enigmacamp.enigshop.models.enums.UserRoles;

import java.util.List;

public record LoginResponse(
        String userId,
        String username,
        List<UserRoles> roles,
        String token
) {
}
