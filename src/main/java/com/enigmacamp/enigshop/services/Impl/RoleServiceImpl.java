package com.enigmacamp.enigshop.services.Impl;

import com.enigmacamp.enigshop.models.entities.Role;
import com.enigmacamp.enigshop.models.enums.UserRoles;
import com.enigmacamp.enigshop.repositories.RoleRepository;
import com.enigmacamp.enigshop.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getOrCreateRole(UserRoles role) {
       return roleRepository.findByRole(role).orElseGet(() -> {
           Role newRole = new Role();
           newRole.setRole(role);
           return roleRepository.save(newRole);
       });
    }

    @Override
    public Role getRoleById(String id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
    }
}
