package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Method to delete a review by its ID
    public void deleteReview(Integer reviewId) {
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
