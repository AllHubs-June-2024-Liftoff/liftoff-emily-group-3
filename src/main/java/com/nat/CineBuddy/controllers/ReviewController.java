package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Actor;
import com.nat.CineBuddy.models.Movie;
import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.repositories.ReviewRepository;
import com.nat.CineBuddy.services.ReviewService;
import com.nat.CineBuddy.services.TMDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Controller
public class ReviewController {

    private final TMDbService tmDbService;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(TMDbService tmDbService, ReviewRepository reviewRepository, ReviewService reviewService) {
        this.tmDbService = tmDbService;
        this.reviewRepository = reviewRepository;
        this.reviewService = reviewService;
    }

    @PostMapping("/delete-review/{id}")
    public String deleteReview(@PathVariable("id") Integer reviewId, RedirectAttributes redirectAttributes) {
        // Call the service to delete the review by ID
        reviewService.deleteReview(reviewId);

        // Add a success message to the redirectAttributes (optional)
        redirectAttributes.addFlashAttribute("message", "Review deleted successfully!");

        // Redirect to the reviews page
        return "redirect:/profile/reviews";
    }


        @GetMapping("/profile/reviews")
        public String viewUserReviews(Principal principal, Model model, @RequestParam(value = "sort", required = false) String sort) {
            // Fetch reviews by the currently authenticated user (principal)
            List<Review> userReviews = reviewRepository.findByUsername(principal.getName());

            // Set movie titles using TMDB service
            for (Review review : userReviews) {
                MovieDTO movieDTO = tmDbService.getMovieDetails(review.getMovieId());  // Get movie details by ID
                review.setMovieTitle(movieDTO.getTitle());  // Set movie title
            }


            // Add reviews to the model
            model.addAttribute("reviews", userReviews);

            return "profile/reviews";  // Return the reviews page
        }

    }
