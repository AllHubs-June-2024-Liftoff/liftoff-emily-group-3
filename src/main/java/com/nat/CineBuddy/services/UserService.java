package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.UserRegistrationDTO;
import com.nat.CineBuddy.models.User;

import java.util.Optional;

public interface UserService {

    void save(User user);

    Optional<User> findByUsername(String username);

    Iterable<User> getAllUsers();

    void deleteUserById(Integer id);

    void refreshUsers();

    boolean registerUser(UserRegistrationDTO userRegistrationDTO);
}