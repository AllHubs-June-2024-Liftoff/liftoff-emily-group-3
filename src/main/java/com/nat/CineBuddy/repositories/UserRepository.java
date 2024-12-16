package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean existsByUsername(String username);
}
