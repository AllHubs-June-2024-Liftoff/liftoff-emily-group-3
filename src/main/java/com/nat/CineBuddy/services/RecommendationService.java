package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.models.WatchList;
import com.nat.CineBuddy.repositories.RecommendedMovieRepository;
import com.nat.CineBuddy.repositories.ReviewRepository;
import com.nat.CineBuddy.repositories.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
     * Stable de-dup by movie id (first-seen wins), preserves insertion order.
     */
    private List<MovieDTO> dedupeStableById(List<MovieDTO> input) {
        Map<String, MovieDTO> seen = new LinkedHashMap<>();
        for (MovieDTO rec : input) {
            seen.putIfAbsent(rec.getId(), rec);
        }
        return new ArrayList<>(seen.values());
    }

    /**
     * Get all movies in the user's watchlist.
     */
    public List<String> getAllWatchlistMovies(Profile profile) {
        List<WatchList> watchLists = watchListRepository.findByProfile(profile);

        List<String> allMovieIds = new ArrayList<>();
        for (WatchList watchList : watchLists) {
            for (Integer movieId : watchList.getMovies()) {
                allMovieIds.add(movieId.toString());
            }
        }
        return allMovieIds;
    }

    /**
     * Get all movies reviewed by the user.
     */
    public List<String> getAllReviewedMovies(Profile profile) {
        List<Review> userReviews = reviewRepository.findByProfileId(profile.getId());
        return userReviews.stream()
                .map(Review::getMovieId)
                .toList();
    }

    public List<MovieDTO> getRecommendationsFromWatchlist(Profile profile) {
        List<String> watchlistMovies = getAllWatchlistMovies(profile);
        List<MovieDTO> recs = new ArrayList<>();
        for (String movieId : watchlistMovies) {
            recs.addAll(tmDbService.getSimilarMovieRecommendations(movieId));
        }
        return dedupeStableById(recs); // stable dedup by id
    }

    public List<MovieDTO> getRecommendationsFromReviews(Profile profile) {
        List<String> reviewedMovies = getAllReviewedMovies(profile);
        List<MovieDTO> recs = new ArrayList<>();
        for (String movieId : reviewedMovies) {
            recs.addAll(tmDbService.getSimilarMovieRecommendations(movieId));
        }
        return dedupeStableById(recs); // stable dedup by id
    }

    /**
     * Get movie recommendations based on the user's watchlist and reviews.
     */
    public List<MovieDTO> getRecommendationsBasedOnWatchlistAndReviews(Profile profile) {
        List<String> watchlistMovies = getAllWatchlistMovies(profile);
        List<String> reviewedMovies  = getAllReviewedMovies(profile);

        Set<String> movieIds = new LinkedHashSet<>(watchlistMovies);
        movieIds.addAll(reviewedMovies);

        List<MovieDTO> recommendations = new ArrayList<>();
        for (String movieId : movieIds) {
            recommendations.addAll(tmDbService.getSimilarMovieRecommendations(movieId));
        }

        // NEW: stable dedup (replace fragile .distinct())
        return dedupeStableById(recommendations);
    }
}
