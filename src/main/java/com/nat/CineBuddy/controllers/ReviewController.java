package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Actor;
import com.nat.CineBuddy.models.Movie;
import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.repositories.ReviewRepository;
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
import java.util.List;

@Controller
public class ReviewController {

    private final TMDbService tmDbService;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewController(TMDbService tmDbService, ReviewRepository reviewRepository) {
        this.tmDbService = tmDbService;
        this.reviewRepository = reviewRepository;
    }

    @PostMapping("/delete-review/{id}")
    public String deleteReview(@PathVariable Long id, @RequestParam String movieId, Model model, RedirectAttributes redirectAttributes) {
        // Delete the review by its ID
        reviewRepository.deleteById(id);

        // Fetch updated reviews and other movie details
        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        Movie movie = tmDbService.getMovieDetails(movieId);
        List<Movie> similarMovies = tmDbService.getSimilarMovieRecommendations(movieId);
        List<Actor> actors = tmDbService.getMovieActors(movieId);

        // Add updated details to the model
        model.addAttribute("reviews", reviews);
        model.addAttribute("movie", movie);
        model.addAttribute("similarMovies", similarMovies);
        model.addAttribute("actors", actors);

        // Optionally, you can add a message to indicate successful deletion
        redirectAttributes.addFlashAttribute("message", "Review deleted successfully!");

        return "movies/movie-details";  // Redirect to the movie details page
    }

    @Controller
    public class UserReviewController {

        @Autowired
        private ReviewRepository reviewRepository;

        @GetMapping("/profile/reviews")
        public String viewUserReviews(Principal principal, Model model) {
            // Fetch reviews by the currently authenticated user (principal)
            List<Review> userReviews = reviewRepository.findByUsername(principal.getName());

            for (Review review : userReviews) {
                Movie movie = tmDbService.getMovieDetails(review.getMovieId());  // Get movie details by ID
                review.setMovieTitle(movie.getTitle());  // Assuming you have a setMovieTitle method in Review
            }

            // Add reviews to the model
            model.addAttribute("reviews", userReviews);

            return "profile/reviews";  // This is the name of your HTML template
        }
    }

}
