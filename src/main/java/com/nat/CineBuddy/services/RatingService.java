package com.nat.CineBuddy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    // Add or update a rating for a movie by a user
    public Rating addRating(Integer userId, Integer movieId, int score, String review) {
        Rating rating = new Rating();
        rating.setUserId(userId);
        rating.setMovieId(movieId);
        rating.setScore(score);
        rating.setReview(review);

        return ratingRepository.save(rating);
    }

    // Get all ratings for a particular movie
    public List<Rating> getRatingsForMovie(Integer movieId) {
        return ratingRepository.findByMovieId(movieId);
    }

    // Get a user's rating for a particular movie
    public Rating getRatingForUserAndMovie(Integer userId, Integer movieId) {
        return (Rating) ratingRepository.findByUserIdAndMovieId(userId, movieId).orElse(null);
    }

    // Update a rating (if it already exists) by user for a movie
    public Rating updateRating(Integer userId, Integer movieId, int score, String review) {
        Rating existingRating = (Rating) ratingRepository.findByUserIdAndMovieId(userId, movieId).orElse(null);

        if (existingRating != null) {
            existingRating.setScore(score);
            existingRating.setReview(review);
            return ratingRepository.save(existingRating);
        }

        // If the rating does not exist, create a new one
        return addRating(userId, movieId, score, review);
    }

    // Delete a rating (by user and movie)
    public void deleteRating(Integer userId, Integer movieId) {
        Rating existingRating = (Rating) ratingRepository.findByUserIdAndMovieId(userId, movieId).orElse(null);
        if (existingRating != null) {
            ratingRepository.delete(existingRating);
        }
    }
}
