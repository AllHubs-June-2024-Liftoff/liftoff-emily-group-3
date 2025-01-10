package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.services.ProfileService;
import com.nat.CineBuddy.services.TMDbService;
import com.nat.CineBuddy.services.UserService;
import com.nat.CineBuddy.services.WatchPartyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/watchparty")
public class WatchPartyController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @Autowired
    private WatchPartyService watchPartyService;

    @Autowired
    private TMDbService tmDbService;

    @GetMapping
    public String index(Model model){
        model.addAttribute("user",userService.getCurrentUser());
        return "watchparty/index";
    }

    @GetMapping("/{watchPartyId}")
    public String viewWatchParty(@PathVariable Integer watchPartyId, Model model){
        WatchParty watchParty = watchPartyService.viewWatchParty(watchPartyId);
        List<MovieDTO> movies = new ArrayList<>();
        for(Integer movieId : watchParty.getMovies()){
            movies.add(tmDbService.getMovieDetails(movieId.toString()));
        }
        model.addAttribute("watchparty",watchParty);
        model.addAttribute("profile", userService.getCurrentUser().getProfile());
        model.addAttribute("movies",movies);
        return "watchparty/details";
    }

    @GetMapping("/host")
    public String newWatchPartyForm(Model model){
        model.addAttribute("user",userService.getCurrentUser());
        model.addAttribute("watchparty", new WatchParty());
        return "watchparty/create";
    }

    @PostMapping("/host")
    public String createWatchParty(@Valid @ModelAttribute("watchparty") WatchParty watchParty, BindingResult result, Errors errors){
        if(!errors.hasErrors() && !result.hasErrors()){
            watchParty.setLeader(userService.getCurrentUser().getProfile());
            if(watchParty.getMovies() == null){
                watchParty.setMovies(new ArrayList<Integer>());
            }
            watchPartyService.createWatchParty(watchParty);
            return "redirect:/watchparty";
        }
        else{
            return "redirect:/watchparty/host";
        }
    }

    @GetMapping("/{watchPartyId}/update")
    public String updateWatchPartyForm(@PathVariable Integer watchPartyId, Model model){
        model.addAttribute("user",userService.getCurrentUser());
        model.addAttribute("watchparty", watchPartyService.viewWatchParty(watchPartyId));
        return "watchparty/update";
    }

    @PostMapping("/{watchPartyId}/update")
    public String userProfileUpdate(@Valid @ModelAttribute("watchparty") WatchParty watchParty, @PathVariable Integer watchPartyId, BindingResult result, Errors errors, Model model){
        if(!errors.hasErrors() && !result.hasErrors()){
            boolean success = watchPartyService.updateWatchParty(watchPartyId, watchParty);
            if(success){
                return "redirect:/watchparty/"+watchPartyId;
            }
            else{
                model.addAttribute("watchparty",watchParty);
                model.addAttribute("profile", userService.getCurrentUser().getProfile());
                return "watchparty/details";
            }
        }
        else{
            model.addAttribute("watchparty",watchParty);
            model.addAttribute("profile", userService.getCurrentUser().getProfile());
            return "watchparty/details";
        }
    }

    @GetMapping("/{watchPartyId}/delete")
    public String deleteWatchParty(@PathVariable Integer watchPartyId){
        boolean success = watchPartyService.deleteWatchParty(watchPartyId);
        if(success){
            return "redirect:/watchparty";
        }
        else{
            return "redirect:/watchparty/"+watchPartyId;
        }
    }

    @GetMapping("/{watchPartyId}/leave")
    public String leaveWatchParty(@PathVariable Integer watchPartyId){
        boolean success = watchPartyService.leaveWatchParty(watchPartyId, userService.getCurrentUser().getProfile());
        if(success){
            return "redirect:/watchparty";
        }
        else{
            return "redirect:/watchparty/"+watchPartyId;
        }
    }

    @GetMapping("/add/{movieId}")
    public String addMovieToWatchPartyForm(@PathVariable Integer movieId, Model model){
        model.addAttribute("movieId",movieId);
        model.addAttribute("user",userService.getCurrentUser());
        return "watchparty/add-movie";
    }

    @PostMapping("/add/{movieId}")
    public String addMovieToWatchParty(@PathVariable Integer movieId, @RequestParam("watchpartyOption") List<Integer> watchPartyIds){
        watchPartyService.addMovieToList(movieId, watchPartyIds);
        return "redirect:/movie-details/"+movieId;
    }

}
