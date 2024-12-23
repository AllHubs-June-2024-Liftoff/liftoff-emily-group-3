package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Movie;
import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.repositories.MovieRepository;
import com.nat.CineBuddy.repositories.ReviewRepository;
import com.nat.CineBuddy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    // Method to create a new review
    public Review createReview(Long movieId, Long userId, int rating, String content) {
        // Fetch Movie by ID
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Fetch User by ID
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create and set up Review object
        Review review = new Review();
        review.setMovie(movie);
        review.setUser(user);
        review.setRating(rating);
        review.setContent(content);
        review.setDateCreated(java.time.LocalDateTime.now());

        // Save Review in the repository
        return reviewRepository.save(review);
    }

    // Method to delete a review by its ID
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    // Method to get reviews sorted by the selected criteria
//    public List<Review> getReviewsBySortCriteria(String sortBy) {
//        // You can customize this logic to apply different sorting criteria
//        Sort sort = switch (sortBy) {
//            case "rating" -> Sort.by(Sort.Order.asc("rating"));
//            case "title" -> Sort.by(Sort.Order.asc("movieTitle"));
//            case "date" -> Sort.by(Sort.Order.desc("dateCreated"));
//            default -> Sort.by(Sort.Order.desc("dateCreated"));
//        };
//
//        return (List<Review>) reviewRepository.findAll(sort);
//    }
}
