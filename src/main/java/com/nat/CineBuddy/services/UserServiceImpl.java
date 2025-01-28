package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.UserRegistrationDTO;
import com.nat.CineBuddy.models.Role;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.repositories.UserRepository;
import com.nat.CineBuddy.security.CBUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private WatchPartyService watchPartyService;


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
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            Set<Role> emptyRoles = new HashSet<>();
            User storedUser = optionalUser.get();
            storedUser.setRoles(emptyRoles);
            watchPartyService.removeFromAll(storedUser.getProfile());
            storedUser.getProfile().setHostedGroups(new ArrayList<>());
            storedUser.getProfile().setJoinedGroups(new ArrayList<>());
            userRepository.save(storedUser);
            userRepository.delete(storedUser);
        }
    }

    @Override
    public boolean registerUser(UserRegistrationDTO userRegistrationDTO){
        if(userRepository.existsByUsername(userRegistrationDTO.getUsername())){
            return false;
        }

        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(this.passwordEncoder.encode(userRegistrationDTO.getPassword()));
        Set<Role> roles = new HashSet<>();
        if(userRegistrationDTO.getRoles() != null){
            for (Integer roleId : userRegistrationDTO.getRoles()) {
                Role role = roleService.getRoleById(roleId);
                roles.add(role);
            }
        }
        user.setRoles(roles);
        userRepository.save(user);
        User profileUser = userRepository.findByUsernameOrEmail(user.getUsername(), user.getUsername()).get();
        return profileService.createProfile(profileUser,userRegistrationDTO.getName());
    }

    @Override
    public boolean updateUser(User user, Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
            User storedUser = optionalUser.get();
            storedUser.setUsername(user.getUsername());
            storedUser.setEmail(user.getEmail());
            if(user.getPassword() != null && !user.getPassword().isEmpty()){
                storedUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
            }
            if(user.getRoles() != null){
                storedUser.setRoles(user.getRoles());
            }
            userRepository.save(storedUser);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean areFieldsUnique(String field){
        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(field,field);
        return optionalUser.isEmpty();
    }

    /*Extra methods I'm trying to determine where I should place*/
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CBUserDetails currentUser = (CBUserDetails) authentication.getPrincipal();
            return userRepository.findByUsernameOrEmail(currentUser.getUsername(), currentUser.getUsername()).get();
        } else {
            return new User();
        }
    }

}
