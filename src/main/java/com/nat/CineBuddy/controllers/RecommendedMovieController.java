package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.RecommendedMovie;
import com.nat.CineBuddy.services.RecommendationService;
import com.nat.CineBuddy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RecommendedMovieController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserService userService;

    /**
     * Show the recommended movies page for the logged-in user.
     */
    @GetMapping("/recommendations")
    public String getRecommendations(Model model) {
        List<RecommendedMovie> recommendations = recommendationService.getRecommendationsForCurrentUser();
        model.addAttribute("recommendedMovies", recommendations);
        return "profile/recommendations";
    }
}