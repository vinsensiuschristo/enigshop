package com.enigmacamp.enigshop.repositories;

import com.enigmacamp.enigshop.models.entities.Role;
import com.enigmacamp.enigshop.models.enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
  Optional<Role> findByRole(UserRoles name);
}