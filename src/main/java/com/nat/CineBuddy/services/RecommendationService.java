package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Review;
//import com.nat.CineBuddy.models.WatchList;
import com.nat.CineBuddy.models.WatchList;
import com.nat.CineBuddy.repositories.RecommendedMovieRepository;
import com.nat.CineBuddy.repositories.ReviewRepository;
import com.nat.CineBuddy.repositories.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RecommendationService {

    @Autowired
    private TMDbService tmDbService;

    @Autowired
    private RecommendedMovieRepository recommendedMovieRepository;

    @Autowired
    private WatchListRepository watchListRepository;

    @Autowired
    private ReviewRepository reviewRepository;




    /**
     * Get all movies in the user's watchlist.
     */
    public List<String> getAllWatchlistMovies(Profile profile) {
        //Fetch watchlists by profile.
        List<WatchList> watchLists = watchListRepository.findByProfile(profile);

        List<String> allMovieIds = new ArrayList<>();
        for (WatchList watchList : watchLists) {
            // Iterate through each movie in the watchlist
            for (Integer movieId : watchList.getMovies()) {
                // Convert each Integer to String and add to allMovieIds
                allMovieIds.add(movieId.toString());
            }
        }
        return allMovieIds;

    }
//     Testing code with hardcoded data
//    public List<String> getAllWatchlistMovies(Profile profile) {
//        // Hardcoded list of movie IDs for testing
//        return List.of("12345", "67890", "112233");
//    }


    /**
     * Get all movies reviewed by the user.
     */
    public List<String> getAllReviewedMovies(Profile profile) {
        // Fetch reviews by the user's username.
        List<Review> userReviews = reviewRepository.findByUsername(profile.getUser().getUsername());

        // Extract movie IDs from the reviews.
        return userReviews.stream()
                .map(Review::getMovieId)
                .toList();
    }


    /**
     * Get movie recommendations based on the user's watchlist and reviews.
     */
    public List<MovieDTO> getRecommendationsBasedOnWatchlistAndReviews(Profile profile) {
        // Fetch all movies in the watchlist.
        List<String> watchlistMovies = getAllWatchlistMovies(profile);

        // Fetch all movies the user has reviewed.
        List<String> reviewedMovies = getAllReviewedMovies(profile);

        // Combine movies from watchlist and reviews to remove duplicates.
        Set<String> movieIds = new HashSet<>(watchlistMovies);
        movieIds.addAll(reviewedMovies);

        // Fetch recommendations for all movies.
        List<MovieDTO> recommendations = new ArrayList<>();
        for (String movieId : movieIds) {
            recommendations.addAll(tmDbService.getSimilarMovieRecommendations(movieId));
        }

        // Return unique recommendations.
        return recommendations.stream().distinct().toList();
    }
}

