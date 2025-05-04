package com.enigmacamp.enigshop.services;

import com.enigmacamp.enigshop.models.dto.request.UserAccountRegistrationRequest;
import com.enigmacamp.enigshop.models.dto.response.UserAccountResponse;
import com.enigmacamp.enigshop.models.entities.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserAccount createUserAccount(UserAccountRegistrationRequest request);
    UserAccount loadUserById(String id);
    UserAccount getUserByUsername(String username);
    UserAccountResponse getUserAccountResponseById(String userId);

    void updateUserAccount(UserAccount userAccount);
}
