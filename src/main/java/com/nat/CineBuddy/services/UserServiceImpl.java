package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.UserRegistrationDTO;
import com.nat.CineBuddy.models.Role;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public User findById(Integer id){
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseGet(User::new);
        user.setPassword("");
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameOrEmail(username, username);
    }

    @Override
    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Integer id){
        userRepository.deleteById(id);
    }

    @Override
    public boolean registerUser(UserRegistrationDTO userRegistrationDTO){
        if(userRepository.existsByUsername(userRegistrationDTO.getUsername())){
            return false;
        }

        User user = new User();
        user.setName(userRegistrationDTO.getName());
        user.setUsername(userRegistrationDTO.getUsername());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(this.passwordEncoder.encode(userRegistrationDTO.getPassword()));

        Set<Role> roles = new HashSet<>();
        if(userRegistrationDTO.getRoles() == null){
            roles.add(roleService.findByName("ROLE_USER"));
        }
        else if(userRegistrationDTO.getRoles().isEmpty()){
            roles.add(roleService.findByName("ROLE_USER"));
        }
        else{
            for (Integer roleId : userRegistrationDTO.getRoles()) {
                Role role = roleService.getRoleById(roleId);
                roles.add(role);
            }
        }
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean updateUser(User user, Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
            User storedUser = optionalUser.get();
            storedUser.setName(user.getName());
            storedUser.setUsername(user.getUsername());
            storedUser.setEmail(user.getEmail());
            if(user.getPassword() != null && user.getPassword().isEmpty()){
                storedUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
            }
            storedUser.setRoles(user.getRoles());
            userRepository.save(storedUser);
            return true;
        }
        else{
            return false;
        }
    }
}
