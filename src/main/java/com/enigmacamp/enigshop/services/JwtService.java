package com.enigmacamp.enigshop.services;

import com.enigmacamp.enigshop.models.dto.response.JwtClaims;
import com.enigmacamp.enigshop.models.entities.UserAccount;

public interface JwtService {
    String generateToken(UserAccount userAccount);
    boolean verifyJwtToken(String token);
    JwtClaims getClaimsByToken(String token);
    String refreshToken(String token);
}
