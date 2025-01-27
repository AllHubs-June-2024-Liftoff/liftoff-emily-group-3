package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Review;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {
    List<Review> findByMovieId(String movieId);
    List<Review> findByProfileId(int profileId);
    int countByProfileIdAndRating(int profileId, int rating);
    List<Review> findByProfileIdOrderByRatingDesc(int profileId);

}