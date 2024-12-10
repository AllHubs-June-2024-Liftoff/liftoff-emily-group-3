package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Role;
import com.nat.CineBuddy.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role getRoleById(Integer id){
        Optional<Role> optionalRole = roleRepository.findById(id);
        return optionalRole.orElseGet(Role::new);
    }

    @Override
    public Iterable<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    @Override
    public void deleteRoleById(Integer id){
        roleRepository.deleteById(id);
    }

    @Override
    public void refreshRoles(){
        roleRepository.findAll();
    }
}
