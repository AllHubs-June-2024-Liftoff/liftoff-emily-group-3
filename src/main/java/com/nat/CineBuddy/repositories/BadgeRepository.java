package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Badge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository extends CrudRepository<Badge, Integer> {
    List<Badge> findByUsername(String username);
    boolean existsByUsernameAndBadgeName(String username, String badgeName);
}
