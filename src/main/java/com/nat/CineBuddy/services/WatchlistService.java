package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Movie;
import com.nat.CineBuddy.models.Watchlist;
import com.nat.CineBuddy.models.WatchlistMovie;
import com.nat.CineBuddy.repositories.MovieRepository;
import com.nat.CineBuddy.repositories.UserRepository;
import com.nat.CineBuddy.repositories.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.nat.CineBuddy.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Autowired
    public WatchlistService(WatchlistRepository watchlistRepository, MovieRepository movieRepository, UserRepository userRepository) {
        this.watchlistRepository = watchlistRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a new or updated Watchlist to the database.
     *
     * @param watchlist The Watchlist object to save
     * @return The saved Watchlist
     */
    public Watchlist save(Watchlist watchlist) {
        return watchlistRepository.save(watchlist);
    }

    /**
     * Find all Watchlists belonging to a specific user by their user ID.
     *
     * @param userId The ID of the user
     * @return A list of Watchlists associated with the user
     */
    public List<Watchlist> findByUserId(Integer userId) {
        return watchlistRepository.findByUserId(userId);
    }

    /**
     * Retrieve a specific Watchlist by its unique ID.
     *
     * @param id The ID of the Watchlist
     * @return The Watchlist object if found
     * @throws IllegalArgumentException If the Watchlist is not found
     */
    public Watchlist findById(Integer id) {
        return watchlistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Watchlist with ID " + id + " not found"));
    }

    /**
     * Delete a specific Watchlist from the database by its unique ID.
     *
     * @param id The ID of the Watchlist to delete
     * @throws IllegalArgumentException If the Watchlist is not found
     */
    public void deleteById(Integer id) {
        if (!watchlistRepository.existsById(id)) {
            throw new IllegalArgumentException("Cannot delete Watchlist with ID " + id + " - not found");
        }
        watchlistRepository.deleteById(id);
    }

    /**
     * Retrieve a list of Movie entities from the database by their IDs.
     *
     * @param movieIds A list of movie IDs
     * @return A list of Movie objects
     */
    public List<Movie> getMoviesByIds(List<String> movieIds) {
        return movieRepository.findAllById(movieIds.stream()
                .map(Integer::parseInt) // Convert string IDs to integers
                .collect(Collectors.toList()));
    }

    /**
     * Update watched status and comments for movies in a watchlist.
     *
     * @param watchlistId     ID of the watchlist being updated
     * @param watchedMovieIds List of movie IDs marked as watched
     * @param comments        Map of movie IDs to their respective comments
     */
    public void updateWatchedStatusAndComments(Integer watchlistId, List<Integer> watchedMovieIds, Map<Integer, String> comments) {
        Watchlist watchlist = findById(watchlistId);

        // Update each movie's status and comments
        watchlist.getMovies().forEach(movie -> {
            movie.setWatched(watchedMovieIds.contains(movie.getId()));
            if (comments.containsKey(movie.getId())) {
                movie.setComment(comments.get(movie.getId()));
            }
        });

        save(watchlist); // Save updated watchlist
    }
    /**
     * Convert a list of Movie objects to WatchlistMovie objects with default values.
     *
     * @param movies List of Movie objects
     * @return List of WatchlistMovie objects
     */
    public List<WatchlistMovie> convertToWatchlistMovies(List<Movie> movies) {
        return movies.stream()
                .map(movie -> new WatchlistMovie(movie, false, "")) // Default values for watched=false and comment=""
                .collect(Collectors.toList());
    }

    /**
     * Create a Watchlist and associate it with movies.
     *
     * @param watchlist The Watchlist to create
     * @param movieIds  List of movie IDs to associate with the Watchlist
     * @param username    username of the user creating the Watchlist
     * @return The saved Watchlist
     */
    public Watchlist createWatchlistWithMovies(Watchlist watchlist, List<String> movieIds, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Fetch movies from the database
        List<Movie> movies = getMoviesByIds(movieIds);

        // Convert movies to WatchlistMovie objects
        List<WatchlistMovie> watchlistMovies = convertToWatchlistMovies(movies);

        // Associate the movies and user with the Watchlist
        watchlist.setUser(user);
        watchlist.setMovies(watchlistMovies);

        return save(watchlist);
    }
}
