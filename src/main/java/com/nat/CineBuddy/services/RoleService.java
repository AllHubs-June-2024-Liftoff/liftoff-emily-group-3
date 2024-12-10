package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Role;

public interface RoleService {
    void save(Role role);

    Role findByName(String name);

    Iterable<Role> getAllRoles();

    void deleteRoleById(Integer id);

    void refreshRoles();
}
