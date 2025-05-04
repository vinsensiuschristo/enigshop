package com.enigmacamp.enigshop.controller;

import com.enigmacamp.enigshop.constants.APIUrl;
import com.enigmacamp.enigshop.models.dto.request.AddRoleToUserRequest;
import com.enigmacamp.enigshop.models.dto.request.AuthRequest;
import com.enigmacamp.enigshop.models.dto.request.RegistrationRequest;
import com.enigmacamp.enigshop.models.dto.response.*;
import com.enigmacamp.enigshop.models.entities.UserAccount;
import com.enigmacamp.enigshop.services.AuthService;
import com.enigmacamp.enigshop.services.JwtService;
import com.enigmacamp.enigshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.AUTH_API)
public class AuthController {
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthService authService;

    @PutMapping("/token")
    public ResponseEntity<CommonResponse<TokenResponse>> generateToken(@RequestBody AuthRequest authRequest) {
        UserAccount userAccount = userService.getUserByUsername(authRequest.username());
        String token = jwtService.generateToken(userAccount);
        return ResponseEntity.ok(new CommonResponse<>(
                HttpStatus.CREATED.value(),
                "Successfully generated token",
                new TokenResponse(token),
                null));
    }

    @GetMapping("/verify")
    public ResponseEntity<CommonResponse<Boolean>> verifyToken(String token) {
        boolean isValid = jwtService.verifyJwtToken(token);
        if (isValid) {
            return ResponseEntity.ok(new CommonResponse<>(
                    HttpStatus.OK.value(),
                    "Token is valid",
                    true,
                    null));
        } else {
            return ResponseEntity.ok(new CommonResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Token is invalid",
                    false,
                    null));
        }
    }

    @GetMapping("/claims")
    public ResponseEntity<CommonResponse<UserAccountResponse>> extractClaims(String token) {
        JwtClaims claimsByToken = jwtService.getClaimsByToken(token);
        UserAccountResponse userAccount = userService.getUserAccountResponseById(claimsByToken.userAccountId());

        if (userAccount != null) {
            return ResponseEntity.ok(new CommonResponse<>(
                    HttpStatus.OK.value(),
                    "Successfully retrieved claims",
                    userAccount,
                    null));
        } else {
            return ResponseEntity.ok(new CommonResponse<>(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Invalid or expired token",
                    null,
                    null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<RegistrationResponse>> register(@RequestBody RegistrationRequest request) {
        RegistrationResponse registrationResponse = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonResponse<>(
                        HttpStatus.CREATED.value(),
                        "Successfully registered",
                        registrationResponse,
                        null));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody AuthRequest authRequest) {
        try {
            LoginResponse loginResponse = authService.login(authRequest);
            return ResponseEntity.ok(new CommonResponse<>(
                    HttpStatus.OK.value(),
                    "Successfully logged in",
                    loginResponse,
                    null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse<>(
                            HttpStatus.UNAUTHORIZED.value(),
                            "Invalid username or password",
                            null,
                            null)
                    );
        }
    }

    @GetMapping("/refresh/{tokenRequest}")
    public ResponseEntity<CommonResponse<TokenResponse>> refreshToken(@PathVariable String tokenRequest) {
        try {
            String token = jwtService.refreshToken(tokenRequest);
            return ResponseEntity.ok(new CommonResponse<>(
                    HttpStatus.CREATED.value(),
                    "Successfully refreshed token",
                    new TokenResponse(token),
                    null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse<>(
                            HttpStatus.UNAUTHORIZED.value(),
                            "Invalid or expired token",
                            null,
                            null)
                    );
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/register-admin")
    public ResponseEntity<CommonResponse<RegistrationResponse>> registerAdmin(@RequestBody RegistrationRequest request) {
        RegistrationResponse registrationResponse = authService.registerAdmin(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonResponse<>(
                        HttpStatus.CREATED.value(),
                        "Successfully registered as admin",
                        registrationResponse,
                        null));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/add-role")
    public ResponseEntity<CommonResponse<RegistrationResponse>> addRoleToUser(@RequestBody AddRoleToUserRequest request) {
        RegistrationResponse registrationResponse = authService.addRoleToUser(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        HttpStatus.OK.value(),
                        "Successfully added role to user",
                        registrationResponse,
                        null));
    }
}
