package com.enigmacamp.enigshop.services.Impl;

import com.enigmacamp.enigshop.exceptions.DuplicateResourceException;
import com.enigmacamp.enigshop.exceptions.ResourceNotFoundException;
import com.enigmacamp.enigshop.models.dto.request.UserAccountRegistrationRequest;
import com.enigmacamp.enigshop.models.dto.response.UserAccountResponse;
import com.enigmacamp.enigshop.models.entities.UserAccount;
import com.enigmacamp.enigshop.repositories.UserAccountRepository;
import com.enigmacamp.enigshop.services.UserService;
import com.enigmacamp.enigshop.utils.ConvertType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserAccountRepository userAccountRepository;

    @Override
    public UserAccount createUserAccount(UserAccountRegistrationRequest request) {
        userAccountRepository.findByUsername(request.username())
                .ifPresent(user -> {
                    throw new DuplicateResourceException("Username already exist");
                });

        return userAccountRepository.save(ConvertType.userAccountRequestToUserAccount(request));
    }

    @Override
    public UserAccount loadUserById(String id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserAccount getUserByUsername(String username) {
        return userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public UserAccountResponse getUserAccountResponseById(String userId) {
        UserAccount userAccount = loadUserById(userId);
        return ConvertType.userAccountToUserAccountResponse(userAccount);
    }

    @Override
    public void updateUserAccount(UserAccount userAccount) {
        userAccountRepository.save(userAccount);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
