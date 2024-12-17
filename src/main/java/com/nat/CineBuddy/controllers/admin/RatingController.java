package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Rating;
import com.nat.CineBuddy.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    // Add or update a rating
    @PostMapping("/{userId}/{movieId}")
    public Rating addOrUpdateRating(@PathVariable Integer userId,
                                    @PathVariable Integer movieId,
                                    @RequestParam int score,
                                    @RequestParam String review) {
        return ratingService.updateRating(userId, movieId, score, review);

    }

    // Get all ratings for a movie
    @GetMapping("/movie/{movieId}")
    public List<Rating> getRatingsForMovie(@PathVariable Integer movieId) {
        return ratingService.getRatingsForMovie(movieId);
    }

    // Get a user's rating for a movie
    @GetMapping("/user/{userId}/movie/{movieId}")
    public Rating getRatingForUser(@PathVariable Integer userId, @PathVariable Integer movieId) {
        return ratingService.getRatingForUserAndMovie(userId, movieId);
    }

    // Delete a rating for a movie by a user
    @DeleteMapping("/user/{userId}/movie/{movieId}")
    public void deleteRating(@PathVariable Integer userId, @PathVariable Integer movieId) {
        ratingService.deleteRating(userId, movieId);
    }
}