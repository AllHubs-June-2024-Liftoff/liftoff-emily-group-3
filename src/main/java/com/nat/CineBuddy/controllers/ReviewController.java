package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Review;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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


//    @GetMapping("/profile/reviews")
//    public String viewUserReviews(Principal principal, Model model, @RequestParam(value = "sort", required = false) String sort) {
//
//        List<Review> userReviews = reviewRepository.findByProfileId(userService.getCurrentUser().getProfile().getId());
//
//        for (Review review : userReviews) {
//            MovieDTO movieDTO = tmDbService.getMovieDetails(review.getMovieId());
//            review.setMovieTitle(movieDTO.getTitle());
//        }
//
//        model.addAttribute("reviews", userReviews);
//
//        return "profile/reviews";
//    }

    @GetMapping("/profile/reviews")
    public String viewReviews(@RequestParam(value = "sortBy", defaultValue = "date") String sortBy, Model model) {
        List<Review> reviews = reviewRepository.findByProfileIdOrderByRatingDesc(userService.getCurrentUser().getProfile().getId());

        // Sort reviews based on the selected sort option
        if ("rating".equals(sortBy)) {
            reviews = reviews.stream()
                    .sorted(Comparator.comparingInt(Review::getRating).reversed()) // Sorting by rating (descending)
                    .collect(Collectors.toList());
        } else if ("date".equals(sortBy)) {
            reviews = reviews.stream()
                    .sorted(Comparator.comparing(Review::getDateCreated).reversed()) // Sorting by date (descending)
                    .collect(Collectors.toList());
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("sortBy", sortBy);  // Pass the selected sort option to the view
        return "profile/reviews";
    }


}