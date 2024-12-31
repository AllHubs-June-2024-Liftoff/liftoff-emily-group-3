package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Actor;
import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.repositories.ReviewRepository;
import com.nat.CineBuddy.repositories.UserRepository;
import com.nat.CineBuddy.services.TMDbService;
import com.nat.CineBuddy.dto.MovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class MovieController {
    private final TMDbService tmDbService;

    @Autowired
    public MovieController(TMDbService tmDbService) {
        this.tmDbService = tmDbService;
    }

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/submit-review")
    public String submitReview(@RequestParam Integer movieId, @RequestParam int rating,
                               @RequestParam String review, Principal principal, Model model) {
        if (principal == null || movieId == null) {
            return "redirect:/error";
        }

        // Fetch movie details from TMDb using MovieDTO
        MovieDTO movieDTO = tmDbService.getMovieDetails(movieId);
        if (movieDTO == null) {
            return "redirect:/error";
        }

        // Fetch the currently authenticated user
        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(principal.getName(), principal.getName());
        if (optionalUser.isEmpty()) {
            return "redirect:/error";
        }

        User user = optionalUser.get();

        // Create and save a new review
        Review newReview = new Review();
        newReview.setMovieId(movieDTO.getId());  // Store movieId, not the full movie object
        newReview.setUser(user);
        newReview.setRating(rating);
        newReview.setContent(review);
        newReview.setDateCreated(LocalDateTime.now());
        reviewRepository.save(newReview);

        // Fetch reviews and other details to render on the page
        List<Review> reviews = reviewRepository.findByMovieId(movieDTO.getId());
        List<MovieDTO> similarMovies = tmDbService.getSimilarMovieRecommendations(movieId);
        List<Actor> actors = tmDbService.getMovieActors(movieId);

        model.addAttribute("reviews", reviews);
        model.addAttribute("movie", movieDTO);  // Add MovieDTO to model
        model.addAttribute("similarMovies", similarMovies);
        model.addAttribute("actors", actors);

        return "movies/movie-details";
    }

    @GetMapping("/movie-details/{id}")
    public String getMovieDetails(@PathVariable Integer id, Model model) {
        MovieDTO movieDTO = tmDbService.getMovieDetails(id);
        if (movieDTO == null) {
            return "redirect:/error";
        }

        List<MovieDTO> similarMovies = tmDbService.getSimilarMovieRecommendations(id);
        List<Actor> actors = tmDbService.getMovieActors(id);
        List<Review> reviews = reviewRepository.findByMovieId(id);

        model.addAttribute("reviews", reviews);
        model.addAttribute("movie", movieDTO);
        model.addAttribute("similarMovies", similarMovies);
        model.addAttribute("actors", actors);

        return "movies/movie-details";
    }
}
