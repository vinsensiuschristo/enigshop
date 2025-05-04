package com.enigmacamp.enigshop.services;

import com.enigmacamp.enigshop.models.entities.Role;
import com.enigmacamp.enigshop.models.enums.UserRoles;

public interface RoleService {
    Role getOrCreateRole(UserRoles role);
    Role getRoleById(String id);
}
