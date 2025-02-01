package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BadgeService badgeService;

    public void deleteReview(Integer reviewId) {

        reviewRepository.deleteById(reviewId);

    }

    public Review createNewReview(String movieId, int rating, String reviewContent, MovieDTO movieDTO) {
        Review review = new Review();
        review.setMovieId(movieId);
        review.setProfile(userService.getCurrentUser().getProfile());
        review.setRating(rating);
        review.setContent(reviewContent);
        review.setDateCreated(LocalDateTime.now());
        review.setMovieTitle(movieDTO.getTitle());
        review.setGenre(movieDTO.getGenres());
        return review;
    }
}