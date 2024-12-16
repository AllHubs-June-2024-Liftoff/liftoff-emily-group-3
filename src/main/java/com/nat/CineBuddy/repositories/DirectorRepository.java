package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Director;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends CrudRepository<Director, Integer> {
}