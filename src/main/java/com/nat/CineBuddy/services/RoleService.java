package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Role;

import java.util.Optional;

public interface RoleService {
    void save(Role role);

    Role findByName(String name);

    Role getRoleById(Integer id);

    Iterable<Role> getAllRoles();

    void deleteRoleById(Integer id);

    void refreshRoles();
}
