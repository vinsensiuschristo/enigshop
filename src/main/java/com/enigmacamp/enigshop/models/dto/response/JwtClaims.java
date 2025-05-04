package com.enigmacamp.enigshop.models.dto.response;

import java.util.List;

public record JwtClaims(
     String userAccountId,
     List<String> roles
) { }
