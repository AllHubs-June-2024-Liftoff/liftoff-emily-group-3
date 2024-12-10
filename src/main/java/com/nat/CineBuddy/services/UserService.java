package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.UserRegistrationDTO;
import com.nat.CineBuddy.models.User;

import java.util.Optional;

public interface UserService {

    void save(User user);

    User findById(Integer id);

    Optional<User> findByUsername(String username);

    Iterable<User> getAllUsers();

    void deleteUserById(Integer id);

    void refreshUsers();

    boolean registerUser(UserRegistrationDTO userRegistrationDTO);

    boolean updateUser(User user, Integer userId);
}