package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Rating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Integer> {
    List<Rating> findByMovieId(Integer movieId);

    Optional<Object> findByUserIdAndMovieId(Integer userId, Integer movieId);
}