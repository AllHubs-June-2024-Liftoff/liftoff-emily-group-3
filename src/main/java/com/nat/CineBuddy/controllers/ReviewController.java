package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.repositories.ProfileRepository;
import com.nat.CineBuddy.repositories.ReviewRepository;
import com.nat.CineBuddy.repositories.UserRepository;
import com.nat.CineBuddy.services.TMDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ReviewController {

    private final TMDbService tmDbService;
    private final ReviewRepository reviewRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewController(TMDbService tmDbService, ReviewRepository reviewRepository, ProfileRepository profileRepository, UserRepository userRepository) {
        this.tmDbService = tmDbService;
        this.reviewRepository = reviewRepository;
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/delete-review/{id}")
    public String deleteReview(@PathVariable("id") Integer reviewId) {
        reviewRepository.deleteById(reviewId);
        return "redirect:/profile/reviews";
    }

    @GetMapping("/profile/reviews")
    public String viewUserReviews(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        Profile currentProfile = profileRepository.findByUser(userRepository.findByUsernameOrEmail(principal.getName(), principal.getName())
                        .orElseThrow(() -> new RuntimeException("User not found")))
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        List<Review> userReviews = reviewRepository.findByProfile(currentProfile);

        userReviews.forEach(review -> {
            MovieDTO movieDTO = tmDbService.getMovieDetails(review.getMovieId());
            review.setMovieDTO(movieDTO); // Ensure Review has this setter
        });

        model.addAttribute("reviews", userReviews);
        return "profile/reviews";
    }


}
