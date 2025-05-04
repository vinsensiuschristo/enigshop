package com.enigmacamp.enigshop.services;

import com.enigmacamp.enigshop.models.dto.request.AddRoleToUserRequest;
import com.enigmacamp.enigshop.models.dto.request.AuthRequest;
import com.enigmacamp.enigshop.models.dto.request.RegistrationRequest;
import com.enigmacamp.enigshop.models.dto.response.LoginResponse;
import com.enigmacamp.enigshop.models.dto.response.RegistrationResponse;

public interface AuthService {
    RegistrationResponse register(RegistrationRequest request);
    LoginResponse login(AuthRequest request);
    RegistrationResponse registerAdmin(RegistrationRequest request);
    RegistrationResponse addRoleToUser(AddRoleToUserRequest request);

}
