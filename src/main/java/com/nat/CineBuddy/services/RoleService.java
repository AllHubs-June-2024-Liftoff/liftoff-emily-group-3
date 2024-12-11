package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Role;

public interface RoleService {

    boolean createRole(Role role);

    boolean updateRole(Role role, Integer roleId);

    Role findByName(String name);

    Role getRoleById(Integer id);

    Iterable<Role> getAllRoles();

    boolean deleteRoleById(Integer id);

}
