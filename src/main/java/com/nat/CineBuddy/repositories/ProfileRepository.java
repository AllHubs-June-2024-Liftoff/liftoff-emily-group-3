package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<Profile, Integer> {
    Optional<Profile> findByName(String name);
}
