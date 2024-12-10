package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(String name);
    boolean existsByName(String name);
}
