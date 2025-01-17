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

}