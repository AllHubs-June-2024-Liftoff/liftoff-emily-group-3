import com.nat.CineBuddy.models.Movie;
import com.nat.CineBuddy.models.Watchlist;
import com.nat.CineBuddy.repositories.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public WatchlistService(WatchlistRepository watchlistRepository, MovieRepository movieRepository) {
        this.watchlistRepository = watchlistRepository;
        this.movieRepository = movieRepository;
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
}
