package com.enigmacamp.enigshop.services.Impl;

import com.enigmacamp.enigshop.exceptions.RequestValidationException;
import com.enigmacamp.enigshop.models.dto.request.AddRoleToUserRequest;
import com.enigmacamp.enigshop.models.dto.request.AuthRequest;
import com.enigmacamp.enigshop.models.dto.request.RegistrationRequest;
import com.enigmacamp.enigshop.models.dto.request.UserAccountRegistrationRequest;
import com.enigmacamp.enigshop.models.dto.response.LoginResponse;
import com.enigmacamp.enigshop.models.dto.response.RegistrationResponse;
import com.enigmacamp.enigshop.models.entities.Role;
import com.enigmacamp.enigshop.models.entities.UserAccount;
import com.enigmacamp.enigshop.models.enums.UserRoles;
import com.enigmacamp.enigshop.services.*;
import com.enigmacamp.enigshop.utils.ConvertType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final CustomerService customerService;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegistrationResponse register(RegistrationRequest request) {
        String hashedPassword = passwordEncoder.encode(request.password());

        //  Assign Role to UserAccount
        Role customerRole = roleService.getOrCreateRole(UserRoles.CUSTOMER);
        UserAccountRegistrationRequest userAccount = new UserAccountRegistrationRequest(
                request.username(),
                hashedPassword,
                request.email(),
                true,
                List.of(customerRole)
        );

        // Create UserAccount
        UserAccount createdUserAccount = userService.createUserAccount(userAccount);

        // Create Customer
        customerService.createCustomer(request);

        return new RegistrationResponse(
                createdUserAccount.getId(),
                createdUserAccount.getUsername(),
                ConvertType.roleToUserRoles(createdUserAccount.getRoles())
        );
    }

    @Override
    public LoginResponse login(AuthRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        UserAccount userAccount = (UserAccount) authenticate.getPrincipal();
        String token = jwtService.generateToken(userAccount);

        return new LoginResponse(
                userAccount.getId(),
                userAccount.getUsername(),
                userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
                        .stream().map(UserRoles::valueOf).toList(),
                token
        );
    }

    @Override
    public RegistrationResponse registerAdmin(RegistrationRequest request) {
        String hashedPassword = passwordEncoder.encode(request.password());

        //  Assign Role to UserAccount
        Role adminRole = roleService.getOrCreateRole(UserRoles.ADMIN);
        UserAccountRegistrationRequest userAccount = new UserAccountRegistrationRequest(
                request.username(),
                hashedPassword,
                request.email(),
                true,
                List.of(adminRole)
        );

        // Create UserAccount
        UserAccount createdUserAccount = userService.createUserAccount(userAccount);

        return new RegistrationResponse(
                createdUserAccount.getId(),
                createdUserAccount.getUsername(),
                ConvertType.roleToUserRoles(createdUserAccount.getRoles())
        );
    }

    @Override
    public RegistrationResponse addRoleToUser(AddRoleToUserRequest request) {
        UserAccount userAccount = userService.loadUserById(request.userId());
        if (!UserRoles.isValidValue(request.role())) {
            throw new RequestValidationException("Invalid Role");
        }
        Role role = roleService.getOrCreateRole(UserRoles.fromValue(request.role()));

        userAccount.getRoles().add(role);
        userService.updateUserAccount(userAccount);

        return new RegistrationResponse(
                userAccount.getId(),
                userAccount.getUsername(),
                ConvertType.roleToUserRoles(userAccount.getRoles())
        );
    }
}
