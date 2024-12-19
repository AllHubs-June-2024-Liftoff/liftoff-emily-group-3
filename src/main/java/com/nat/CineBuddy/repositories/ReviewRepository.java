package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, String> {
    List<Review> findByMovieId(String movieId);
}
