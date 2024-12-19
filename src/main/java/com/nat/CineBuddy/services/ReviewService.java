package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.repositories.ReviewRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ReviewService {

    private static final String TMDB_API_URL = "https://api.themoviedb.org/3/movie/{movieId}/rating";
    private static final String BEARER_TOKEN = "YOUR_ACCESS_TOKEN";

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public String leaveReview(int movieId, double rating) {
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + BEARER_TOKEN);
        headers.set("Content-Type", "application/json;charset=utf-8");

        // Create body
        String body = "{\"value\": " + rating + "}";

        // Create HttpEntity
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        // Send POST request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                TMDB_API_URL,
                HttpMethod.POST,
                requestEntity,
                String.class,
                movieId
        );

        // Return response body or status
        return response.getBody();
    }

    // Method to get reviews by movie ID
    public List<Review> getReviewsByMovieId(String movieId) {
        // Query the local database for reviews
        return reviewRepository.findByMovieId(movieId);
    }

    public void createReview(Review review) {
        reviewRepository.save(review);
    }

    public void updateReview(Long id, Review updatedReview) {
        reviewRepository.save(updatedReview);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(String.valueOf(id));
    }
}
