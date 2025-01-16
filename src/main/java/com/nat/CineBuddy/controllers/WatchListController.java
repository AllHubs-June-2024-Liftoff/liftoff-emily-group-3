package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.models.WatchList;
import com.nat.CineBuddy.services.ProfileService;
import com.nat.CineBuddy.services.TMDbService;
import com.nat.CineBuddy.services.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/watchlists")
public class WatchListController {

    @Autowired
    private WatchListService watchListService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private TMDbService tmdbService;

    @GetMapping("/create")
    public String showCreateWatchListForm(Model model) {
        model.addAttribute("watchList", new WatchList());
        return "watchlist/watchlist-create";
    }

    @PostMapping("/create")
    public String createWatchList(@RequestParam String name) {
        Profile currentProfile = getCurrentUserProfile();
        if (currentProfile == null) {
            throw new IllegalStateException("No profile found for the current user.");
        }
        watchListService.createWatchList(name, currentProfile);
        return "redirect:/profile";
    }

    @GetMapping("/{id}")
    public String viewWatchList(@PathVariable Integer id, Model model) {
        WatchList watchList = watchListService.getWatchListById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid WatchList ID: " + id));

        List<MovieDTO> movies = watchListService.getMoviesFromWatchList(watchList);
        model.addAttribute("watchList", watchList);
        model.addAttribute("movies", movies);
        return "watchlist-view";
    }

    @PostMapping("/{watchListId}/add-movie")
    public String addMovieToWatchList(@PathVariable Integer watchListId, @RequestParam String movieId) {
        WatchList watchList = watchListService.getWatchListById(watchListId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid WatchList ID: " + watchListId));

        MovieDTO movie = tmdbService.getMovieDetails(movieId);
        if (movie == null) {
            throw new IllegalArgumentException("Unable to fetch movie details for ID: " + movieId);
        }

        watchListService.addMovieToWatchList(watchList, movie);
        return "redirect:/watchlists/" + watchListId;
    }

    private Profile getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            return profileService.getProfileById(user.getId());
        }
        return null;
    }
}
