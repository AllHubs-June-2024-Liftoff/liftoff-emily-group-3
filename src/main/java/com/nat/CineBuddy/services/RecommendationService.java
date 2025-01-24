package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Review;
//import com.nat.CineBuddy.models.WatchList;
import com.nat.CineBuddy.repositories.RecommendedMovieRepository;
import com.nat.CineBuddy.repositories.ReviewRepository;
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

//    @Autowired
//    private UserService userService;

//    @Autowired
//    private WatchlistRepository watchlistRepository;

    @Autowired
    private ReviewRepository reviewRepository;


//    /**
//     * Update recommendations when a user adds a movie to their watchlist or reviews a movie.
//     */
//    public void updateRecommendations(String movieId){
//        //Get the current users profile
//        Profile profile = userService.getCurrentUser().getProfile();
//
//        //Getting movies using TMdbService.
//        List<MovieDTO> recommendedMovies = tmDbService.getSimilarMovieRecommendations(movieId);
//
//        for(MovieDTO movie : recommendedMovies){
//            //Check if movie already exists in the users recommendations.
//            if(!recommendedMovieRepository.existsByProfileAndMovieId(profile, movie.getId())){
//
//                //Add the movie to the recommendations.
//                RecommendedMovie recommendedMovie = new RecommendedMovie();
//                recommendedMovie.setProfile(profile);
//                recommendedMovie.setMovieId(movie.getId());
//                recommendedMovie.setTitle(movie.getTitle());
//                recommendedMovie.setPosterPath(movie.getPosterPath());
//                recommendedMovieRepository.save(recommendedMovie);
//            }
//        }
//    }
//
//    //Fetch all recommended movies for the current user.
//
//    public List<RecommendedMovie> getRecommendationsForCurrentUser(){
//        Profile profile = userService.getCurrentUser().getProfile();
//        return recommendedMovieRepository.findByProfile(profile);
//    }

    /**
     * Get all movies in the user's watchlist.
     */
    public List<String> getAllWatchlistMovies(Profile profile) {
        //Fetch watchlists by profile.
        List<WatchList> watchLists = watchlistRepository.findByProfile(profile);

        List<String> allMovieIds = new ArrayList<>();
        for(WatchList watchList : watchLists){
            allMovieIds.addAll(watchList.getMovies());

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

