package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.UserRegistrationDTO;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(user.getPassword());
        userRepository.save(user);
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
    public void refreshUsers(){
        userRepository.findAll();
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
        if(userRegistrationDTO.getRoles() == null){
            userRegistrationDTO.setRoles(Collections.singleton(roleService.findByName("ROLE_USER")));
        }
        if(userRegistrationDTO.getRoles().isEmpty()){
            user.setRoles(Collections.singleton(roleService.findByName("ROLE_USER")));
        }
        System.out.println(user.getRoles());
        userRepository.save(user);
        return true;
    }
}
