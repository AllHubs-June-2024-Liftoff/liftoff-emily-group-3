package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findByMovieId(String movieId);
    List<Review> findByUsername(String username);
}
