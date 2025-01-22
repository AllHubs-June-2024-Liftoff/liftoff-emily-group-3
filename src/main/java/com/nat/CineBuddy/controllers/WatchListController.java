package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.models.WatchList;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/watchlist")
public class WatchListController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @Autowired
    private WatchListService watchListService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private TMDbService tmDbService;

    @GetMapping
    public String index(Model model){
        model.addAttribute("user",userService.getCurrentUser());
        return "watchlist/index";
    }

    @GetMapping("/watchlist")
    public String displayWatchLists(Model model) {
        String currentUser = userService.getCurrentUser().getUsername();
        List<WatchList> watchLists = watchListService.getWatchListsByUser(currentUser);
        model.addAttribute("watchLists", watchLists);
        return "watchlist/index";
    }


    @GetMapping("/{watchListId}")
    public String viewWatchList(@PathVariable Integer watchListId, Model model){
        WatchList watchList = watchListService.getWatchList(watchListId);
        Profile profile = userService.getCurrentUser().getProfile();
        List<MovieDTO> movies = new ArrayList<>();
            for(Integer movieId : watchList.getMovies()){
                movies.add(tmDbService.getMovieDetails(movieId.toString()));
            }
            model.addAttribute("watchlist",watchList);
            model.addAttribute("profile", profile);
            model.addAttribute("movies",movies);
            return "watchlist";

        }

    @GetMapping("/create")
    public String showCreateWatchListForm(Model model) {
        model.addAttribute("user",userService.getCurrentUser());
        model.addAttribute("watchList", new WatchList());
        return "watchlist/create";
    }

    @PostMapping("/create")
    public String createWatchList(@Valid @ModelAttribute("watchList") WatchList watchList, BindingResult result, Errors errors) {
        if (!errors.hasErrors() && !result.hasErrors()) {
            watchList.setProfile(userService.getCurrentUser().getProfile());
            watchListService.createWatchList(watchList);
            return "redirect:/watchlist";
        } else {
            return "redirect:/watchlist/create";
        }
    }

    @GetMapping("/{watchListId}/delete")
    public String deleteWatchList(@PathVariable Integer watchListId){
        boolean success = watchListService.deleteWatchList(watchListId);
        if(success){
            return "redirect:/watchlist";
        }
        else{
            return "redirect:/watchlist/"+watchListId;
        }
    }

    @GetMapping("/add/{movieId}")
    public String addMovieToWatchList(@PathVariable Integer movieId, Model model){
        model.addAttribute("movieId",movieId);
        model.addAttribute("user",userService.getCurrentUser());
        return "watchlist/add-movie";
    }

    @PostMapping("/add/{movieId}")
    public String addMovieToWatchList(@PathVariable Integer movieId, @RequestParam(name = "watchListOption", required = false) List<Integer> watchListIds){
        watchListService.addMovieToList(movieId, watchListIds);
        return "redirect:/movie-details/"+movieId;
    }

    @GetMapping("/{watchListId}/movies/remove/{movieId}")
    public String removeMovieFromWatchList(@PathVariable Integer watchListId, @PathVariable Integer movieId){
        watchListService.removeMovie(watchListId, movieId);
        return "redirect:/watchlist/"+watchListId;
    }

}

