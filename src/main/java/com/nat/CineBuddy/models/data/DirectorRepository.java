package com.nat.CineBuddy.models.data;

import com.nat.CineBuddy.models.Director;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends CrudRepository<Director, Integer> {
}