package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Actor;
import com.nat.CineBuddy.models.Movie;
import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.repositories.MovieRepository;
import com.nat.CineBuddy.repositories.ReviewRepository;
import com.nat.CineBuddy.services.TMDbService;
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

    public MovieController(TMDbService tmDbService) {
        this.tmDbService = tmDbService;
    }


    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @PostMapping("/submit-review")
    public String submitReview(@RequestParam String movieId, @RequestParam int rating,
                               @RequestParam String review, Principal principal, Model model) {
        Review newReview = new Review();
        newReview.setMovieId(movieId);
        newReview.setRating(rating);
        newReview.setContent(review);
        newReview.setUsername(principal.getName());  // Assuming you have authentication enabled
        newReview.setDateCreated(LocalDateTime.now());

        reviewRepository.save(newReview);

        // Fetch all reviews for the movie
        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        Movie movie = tmDbService.getMovieDetails(movieId);
        List<Movie> similarMovies = tmDbService.getSimilarMovieRecommendations(movieId);
        List<Actor> actors = tmDbService.getMovieActors(movieId);

        model.addAttribute("reviews", reviews);
        model.addAttribute("movie", movie);
        model.addAttribute("similarMovies", similarMovies);
        model.addAttribute("actors", actors);
//        model.addAttribute("movie", movieRepository.findById(Long.valueOf(movieId)));

        return "movies/movie-details";  // The movie detail page
    }



    @GetMapping("/movie-details/{id}")
    public String getMovieDetails(@PathVariable String id, Model model) {
        Movie movie = tmDbService.getMovieDetails(id);
        List<Movie> similarMovies = tmDbService.getSimilarMovieRecommendations(id);
        List<Actor> actors = tmDbService.getMovieActors(id);
        List<Review> reviews = reviewRepository.findByMovieId(id);
        model.addAttribute("reviews", reviews);
        model.addAttribute("movie", movie);
        model.addAttribute("similarMovies", similarMovies);
        model.addAttribute("actors", actors);
        return "movies/movie-details";
    }

}