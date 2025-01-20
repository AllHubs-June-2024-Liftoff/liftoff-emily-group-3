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
@RequestMapping("/watchlist")
public class WatchListController {

    private final WatchListService watchListService;
    private final ProfileService profileService;

    @Autowired
    public WatchListController(WatchListService watchListService, ProfileService profileService) {
        this.watchListService = watchListService;
        this.profileService = profileService;
    }

    @GetMapping("/create")
    public String showCreateWatchListForm(Model model) {
        model.addAttribute("watchList", new WatchList());
        return "watchlist/create";
    }

    @PostMapping("/create")
    public String createWatchList(@RequestParam String name) {
        Profile currentProfile = getCurrentUserProfile();
        if (currentProfile == null) {
            throw new IllegalStateException("No profile found for the current user.");
        }
        watchListService.createWatchList(name, currentProfile);
        return "redirect:/profile/index";
    }

    @GetMapping("/index")
    public String listAllWatchLists(Model model) {
        Profile currentProfile = getCurrentUserProfile();
        if (currentProfile == null) {
            throw new IllegalStateException("No profile found for the current user.");
        }
        List<WatchList> watchLists = watchListService.getWatchListsByProfile(currentProfile);
        model.addAttribute("watchLists", watchLists);
        return "watchlist/index";
    }


    @GetMapping("/{id}")
    public String viewWatchList(@PathVariable Integer id, Model model) {
        WatchList watchList = watchListService.getWatchListById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid WatchList ID: " + id));

        List<MovieDTO> movies = watchListService.getMoviesFromWatchList(watchList);
        model.addAttribute("watchList", watchList);
        model.addAttribute("movies", movies);
        return "watchlist/index";
    }

    @PostMapping("/{watchListId}/add-movie")
    public String addMovieToWatchList(@PathVariable Integer watchListId, @RequestParam String movieId) {
        if (watchListId == null || watchListId == 0) {
            throw new IllegalArgumentException("A valid WatchList ID must be provided.");
        }

        WatchList watchList = watchListService.getWatchListById(watchListId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid WatchList ID: " + watchListId));

        watchListService.addMovieToWatchList(watchList, movieId);
        return "redirect:/watchlist/" + watchListId;
    }

    @PostMapping("/{watchListId}/remove-movie")
    public String removeMovieFromWatchList(@PathVariable Integer watchListId, @RequestParam String movieId) {
        if (watchListId == null || watchListId == 0) {
            throw new IllegalArgumentException("A valid WatchList ID must be provided.");
        }

        WatchList watchList = watchListService.getWatchListById(watchListId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid WatchList ID: " + watchListId));

        watchListService.removeMovieFromWatchList(watchList, movieId);
        return "redirect:/watchlist/" + watchListId;
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

