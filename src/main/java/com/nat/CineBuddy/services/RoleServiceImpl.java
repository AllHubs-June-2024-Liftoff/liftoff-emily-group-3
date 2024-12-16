package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Role;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.repositories.RoleRepository;
import com.nat.CineBuddy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean createRole(Role role) {
        if(roleRepository.existsByName(role.getName())){
            return false;
        }
        roleRepository.save(role);
        return true;
    }

    @Override
    public boolean updateRole(Role role, Integer roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if(optionalRole.isPresent()){
            Role storedRole = optionalRole.get();
            storedRole.setName(role.getName());
            roleRepository.save(storedRole);
            return true;
        }
        else{
            return false;
        }
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
    public boolean deleteRoleById(Integer id){
        Optional<Role> optionalRole = roleRepository.findById(id);
        if(!optionalRole.isPresent()){
            return false;
        }
        else{
            Role role = optionalRole.get();
            if(role.getUsers().isEmpty()){
                roleRepository.deleteById(id);
                return true;
            }
            else{
                return false;
            }
        }
    }

}
