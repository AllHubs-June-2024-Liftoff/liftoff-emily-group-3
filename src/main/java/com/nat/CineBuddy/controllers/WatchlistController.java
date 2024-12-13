package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Watchlist;
import com.nat.CineBuddy.services.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Written By Cody Adams
 * Handles user interactions with Watchlists (view, create, delete).
 * Will be connected with UserService.
 */

@Controller
@RequestMapping("/watchlists")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;  //Service to handle database operations for Watchlists

    @Autowired
    private UserService userService; //Service to get user details

    //Displays all watchlist for the logged in user.

    @GetMapping
    public String getAllWatchlists(Principal principal, Model model) {

        //Get logged in user details
        User user = userService.findByUsername(principal.getName()).orElse(null);

        //Get all watchlists for the user
        List<Watchlist> watchlists = watchlistService.findByUserId(user.getId());

        //Add the list of watchlists to the view
        model.addAttribute("watchlists", watchlists);

        //Show watchlist paage
        return "watchlists";
    }

    //Displays a form to creat a new watchlist

    @GetMapping("/create")
    public String createForm(Model model) {

        //Add an empty Watchlist object for the form
        model.addAttribute("watchlist", new Watchlist());
        return "watchlist_form";
    }

    @PostMapping("/create")
    public String createWatchlist(@ModelAttribute Watchlist watchlist, Principal principal) {

        //Get the logged in users details
        User user = userService.findByUsername(principal.getName()).orElse(null);

        //Link watchlist to user
        watchlist.setUser(user);

        //Save the watchlist to database
        watchlistService.save(watchlist);
        return "redirect:/watchlists";
    }

    //Deletes the watchlist by its ID.

    @GetMapping("/delete/{id}")
    public String deleteWatchlist(@PathVariable Integer id) {
        watchlistService.deleteById(id);
        return "redirect:/watchlists";
    }
}
