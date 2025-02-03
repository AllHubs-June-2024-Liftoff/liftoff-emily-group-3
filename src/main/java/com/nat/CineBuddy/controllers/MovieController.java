
package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Actor;
import com.nat.CineBuddy.models.Movie;
import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.repositories.MovieRepository;
import com.nat.CineBuddy.repositories.ReviewRepository;
import com.nat.CineBuddy.services.BadgeService;
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

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class MovieController {
    private final TMDbService tmDbService;
    private final BadgeService badgeService;
    private final UserService userService;
    private final ReviewService reviewService;

    public MovieController(TMDbService tmDbService, BadgeService badgeService, UserService userService, ReviewService reviewService) {
        this.tmDbService = tmDbService;
        this.badgeService = badgeService;
        this.userService = userService;
        this.reviewService = reviewService;
    }


    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;


    @PostMapping("/submit-review")
    public String submitReview(@RequestParam String movieId, @RequestParam int rating,
                               @RequestParam String review, Principal principal, Model model) {

        if (principal == null) {
            return "redirect:/login";
        }

        MovieDTO movieDTO = tmDbService.getMovieDetails(movieId);
        if (movieDTO == null) {
            model.addAttribute("error", "Movie details could not be found.");
            return "error";
        }

        Review newReview = reviewService.createNewReview(movieId, rating, review, movieDTO);
        reviewRepository.save(newReview);

        badgeService.awardBadge(userService.getCurrentUser().getProfile().getId());

        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        List<MovieDTO> similarMovies = tmDbService.getSimilarMovieRecommendations(movieId);
        List<Actor> actors = tmDbService.getMovieActors(movieId);

        model.addAttribute("reviews", reviews);
        model.addAttribute("movie", movieDTO);
        model.addAttribute("similarMovies", similarMovies);
        model.addAttribute("actors", actors);

        return "movies/movie-details";
    }






    @GetMapping("/movie-details/{id}")
    public String getMovieDetails(@PathVariable String id, @RequestParam(value = "sortBy", defaultValue = "date") String sortBy, Model model) {
        MovieDTO movieDTO = tmDbService.getMovieDetails(id);
        List<MovieDTO> similarMovies = tmDbService.getSimilarMovieRecommendations(id);
        List<Actor> actors = tmDbService.getMovieActors(id);

        List<Review> reviews = reviewRepository.findByMovieId(id);

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
        model.addAttribute("movie", movieDTO);
        model.addAttribute("similarMovies", similarMovies);
        model.addAttribute("actors", actors);
        model.addAttribute("sortBy", sortBy);

        Optional<Movie> movieOptional = movieRepository.findById(Integer.valueOf(id));
        movieOptional.ifPresent(movie -> model.addAttribute("repoMovie", movie));

        return "movies/movie-details";
    }

}
