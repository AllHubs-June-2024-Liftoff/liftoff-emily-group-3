package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.repositories.ReviewRepository;
import com.nat.CineBuddy.services.ReviewService;
import com.nat.CineBuddy.services.TMDbService;
import com.nat.CineBuddy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ReviewController {

    private final TMDbService tmDbService;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;
    private final UserService userService;

    @Autowired
    public ReviewController(TMDbService tmDbService, ReviewRepository reviewRepository, ReviewService reviewService, UserService userService) {
        this.tmDbService = tmDbService;
        this.reviewRepository = reviewRepository;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @PostMapping("/delete-review/{id}")
    public String deleteReview(@PathVariable("id") Integer reviewId, RedirectAttributes redirectAttributes) {
        reviewService.deleteReview(reviewId);

        redirectAttributes.addFlashAttribute("message", "Review deleted successfully!");

        return "redirect:/profile/reviews";
    }


    @GetMapping("/profile/reviews")
    public String viewUserReviews(Principal principal, Model model, @RequestParam(value = "sort", required = false) String sort) {

        List<Review> userReviews = reviewRepository.findByProfileId(userService.getCurrentUser().getProfile().getId());

        // Set movie titles using TMDB service
        for (Review review : userReviews) {
            MovieDTO movieDTO = tmDbService.getMovieDetails(review.getMovieId());  // Get movie details by ID
            review.setMovieTitle(movieDTO.getTitle());  // Set movie title
        }


        // Add reviews to the model
        model.addAttribute("reviews", userReviews);

        return "profile/reviews";
    }

}