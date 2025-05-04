package com.enigmacamp.enigshop.models.dto.response;

import com.enigmacamp.enigshop.models.enums.UserRoles;

import java.util.List;

public record UserAccountResponse(
        String userAccountId,
        List<UserRoles> roles
) {
}
