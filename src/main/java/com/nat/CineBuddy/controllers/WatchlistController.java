package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Movie;
import com.nat.CineBuddy.models.Watchlist;
import com.nat.CineBuddy.dto.MovieDto;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.services.TMDbService;
import com.nat.CineBuddy.services.UserService;
import com.nat.CineBuddy.services.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handles all operations related to Watchlists, including:
 * - Viewing all watchlists for a logged-in user
 * - Creating a new watchlist with movies selected from the TMDB API
 * - Viewing a specific watchlist and its contents
 * - Searching for movies via the TMDB API
 * - Updating watched status and comments for movies
 * - Deleting an existing watchlist
 *
 * Author: Cody Adams
 */

@Controller
@RequestMapping("/watchlists")
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final TMDbService tmDbService;
    private final UserService userService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService, TMDbService tmDbService, UserService userService) {
        this.watchlistService = watchlistService;
        this.tmDbService = tmDbService;
        this.userService = userService;
    }

    /**
     * Display all watchlists for the logged-in user.
     *
     * @param principal Authenticated user's details
     * @param model     Model to pass data to the Thymeleaf template
     * @return The "watchlists" Thymeleaf template showing all watchlists
     */
    @GetMapping
    public String getAllWatchlists(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Watchlist> watchlists = watchlistService.findByUserId(user.getId());
        model.addAttribute("watchlists", watchlists);
        return "watchlists"; // Renders watchlists.html
    }

    /**
     * Render the form to create a new watchlist.
     *
     * @param model Model to pass the form and initial data to the Thymeleaf template
     * @return The "create" Thymeleaf template for the watchlist form
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("watchlist", new Watchlist()); // Initial empty Watchlist object
        model.addAttribute("movies", new ArrayList<>());  // Empty list of movies for search results
        return "create"; // Renders create.html
    }

    /**
     * Handle movie searches via the TMDB API.
     * The results will be dynamically displayed in the "create" form.
     *
     * @param query Search term provided by the user
     * @return A list of movies matching the query
     */
    @GetMapping("/search")
    @ResponseBody
    public List<MovieDto> searchMovies(@RequestParam String query) {
        // Fetch movies from TMDbService
        List<Movie> movies = tmDbService.searchMovies(query); // Or call a specific search method

        // Convert Movie to MovieDto
        return movies.stream()
                .map(movie -> new MovieDto(movie.getTitle(), movie.getReleaseDate(), movie.getPosterPath(), movie.getOverview()))
                .collect(Collectors.toList());
    }


    /**
     * Handle the creation of a new watchlist.
     * This includes saving the selected movies along with the watchlist details.
     *
     * @param watchlist Watchlist data (name, description)
     * @param movieIds  List of movie IDs selected in the form
     * @param principal Authenticated user's details
     * @return Redirect to the watchlist overview page
     */
    @PostMapping("/create")
    public String createWatchlist(@ModelAttribute Watchlist watchlist,
                                  @RequestParam List<String> movieIds,
                                  Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        String username = principal.getName();

        // Delegate the creation logic to the service
        watchlistService.createWatchlistWithMovies(watchlist, movieIds, username);

        return "redirect:/watchlists";
    }

    /**
     * View details of a specific watchlist.
     * This includes the watchlist name, description, movies, comments, and watched status.
     *
     * @param id    Watchlist ID
     * @param model Model to pass data to the Thymeleaf template
     * @return The "watchlist_details" Thymeleaf template showing the watchlist and its movies
     */
    @GetMapping("/{id}")
    public String viewWatchlistDetails(@PathVariable Integer id, Model model) {
        Watchlist watchlist = watchlistService.findById(id);
        model.addAttribute("watchlist", watchlist);
        return "watchlist_details"; // Renders watchlist_details.html
    }

    @GetMapping("/{watchlistId}/movies/{movieId}")
    public String viewMovieDetails(@PathVariable Integer watchlistId,
                                   @PathVariable Integer movieId,
                                   Model model) {
        // Ensure the watchlist exists
        Watchlist watchlist = watchlistService.findById(watchlistId);

        // Fetch the movie details from the TMDB API
        Movie movieDetails = tmDbService.getMovieDetails(String.valueOf(movieId));


        // Add data to the model
        model.addAttribute("watchlist", watchlist);
        model.addAttribute("movie", movieDetails);
        model.addAttribute("watchlistId", watchlistId);

        return "movie_details"; // Renders movie_details.html
    }



    /**
     * Update watched status and comments for movies in the watchlist.
     *
     * @param id               Watchlist ID
     * @param watchedMovieIds  List of movie IDs marked as watched
     * @param comments         Map of movie IDs to comments
     * @return Redirect to the watchlist details page
     */
    @PostMapping("/{id}/update")
    public String updateWatchlist(@PathVariable Integer id,
                                  @RequestParam List<Integer> watchedMovieIds,
                                  @RequestParam Map<Integer, String> comments) {
        watchlistService.updateWatchedStatusAndComments(id, watchedMovieIds, comments);
        return "redirect:/watchlists/" + id;
    }

    /**
     * Delete a specific watchlist.
     *
     * @param id Watchlist ID to delete
     * @return Redirect to the watchlist overview page
     */
    @GetMapping("/delete/{id}")
    public String deleteWatchlist(@PathVariable Integer id) {
        watchlistService.deleteById(id);
        return "redirect:/watchlists";
    }
}