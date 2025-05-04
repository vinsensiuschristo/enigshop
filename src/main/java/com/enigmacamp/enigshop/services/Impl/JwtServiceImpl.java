package com.enigmacamp.enigshop.services.Impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.enigmacamp.enigshop.exceptions.RequestValidationException;
import com.enigmacamp.enigshop.models.dto.response.JwtClaims;
import com.enigmacamp.enigshop.models.entities.Role;
import com.enigmacamp.enigshop.models.entities.UserAccount;
import com.enigmacamp.enigshop.services.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${app.enigshop.jwt.jwt-secret}")
    private String secretKey;

    @Value("${app.enigshop.jwt.expired}")
    private Long expired;

    private Algorithm algorithm;

    @Override
    public String generateToken(UserAccount userAccount) {
        algorithm = Algorithm.HMAC256(secretKey);
        Instant instant = Instant.now().plusSeconds(expired);

        List<Role> roles = userAccount.getRoles();

        return JWT.create()
                .withClaim("userAccountId", userAccount.getId())
                .withClaim("roles", roles.toString())
                .withExpiresAt(instant)
                .sign(algorithm);
    }

    @Override
    public boolean verifyJwtToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptExpiresAt(expired)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            log.error("Token tidak valid: {}", exception.getMessage());
            return false;
        }
    }

    @Override
    public JwtClaims getClaimsByToken(String token) {
        return parseJwt(token);
    }

    @Override
    public String refreshToken(String token) {
        if (verifyJwtToken(token)) {
            DecodedJWT decodedJWT = JWT.decode(token);
            String userAccountId = decodedJWT.getClaim("userAccountId").asString();
            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
            Instant instant = Instant.now().plusSeconds(expired + 1200);

            return JWT.create()
                    .withClaim("userAccountId", userAccountId)
                    .withClaim("roles", roles)
                    .withExpiresAt(instant)
                    .sign(algorithm);
        }else{
            throw new RequestValidationException("Token is not valid or expired");
        }
    }

    private JwtClaims parseJwt(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        DecodedJWT decodedJWT = JWT.decode(token);
        String userAccountId = decodedJWT.getClaim("userAccountId").asString();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        return new JwtClaims(userAccountId, roles);
    }
}