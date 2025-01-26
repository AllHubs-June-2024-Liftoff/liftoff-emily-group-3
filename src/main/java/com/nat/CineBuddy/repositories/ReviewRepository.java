package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Review;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    List<Review> findByMovieId(String movieId);
    List<Review> findByUsername(String username);
    List<Review> findByUsernameOrderByRatingDesc(String username);
}