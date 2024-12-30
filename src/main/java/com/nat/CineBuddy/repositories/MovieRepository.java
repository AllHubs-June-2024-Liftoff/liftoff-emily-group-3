package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Integer> { }
