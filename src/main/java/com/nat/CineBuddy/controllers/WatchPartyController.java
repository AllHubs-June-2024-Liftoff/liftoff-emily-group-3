package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.services.*;
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
    private VoteService voteService;

    @Autowired
    private TMDbService tmDbService;

    @GetMapping
    public String index(Model model){
        model.addAttribute("user",userService.getCurrentUser());
        return "watchparty/index";
    }

    @GetMapping("/{watchPartyId}")
    public String viewWatchParty(@PathVariable Integer watchPartyId, Model model){
        WatchParty watchParty = watchPartyService.getWatchParty(watchPartyId);
        Profile profile = userService.getCurrentUser().getProfile();
        if(watchParty.getMembers().contains(profile) || watchParty.getLeader().equals(profile)){
            List<MovieDTO> movies = new ArrayList<>();
            for(Integer movieId : watchParty.getMovies()){
                movies.add(tmDbService.getMovieDetails(movieId.toString()));
            }
            List<MovieDTO> topRatedMovies = watchPartyService.getTopRatedMovies(watchParty);
            model.addAttribute("watchparty",watchParty);
            model.addAttribute("profile", profile);
            model.addAttribute("movies",movies);
            model.addAttribute("topRatedMovies",topRatedMovies);
            model.addAttribute("votes",voteService.getAllVotes(watchParty));
            return "watchparty/details";
        }
        else{
            return "watchparty";
        }
    }

    @GetMapping("/host")
    public String newWatchPartyForm(Model model){
        model.addAttribute("user",userService.getCurrentUser());
        model.addAttribute("watchparty", new WatchParty());
        return "watchparty/create";
    }

    @PostMapping("/host")
    public String createWatchParty(@Valid @ModelAttribute("watchparty") WatchParty watchParty, BindingResult result, Errors errors, Model model){
        if(!errors.hasErrors() && !result.hasErrors()){
            watchParty.setLeader(userService.getCurrentUser().getProfile());
            if(watchParty.getMovies() == null){
                watchParty.setMovies(new ArrayList<Integer>());
            }
            watchPartyService.createWatchParty(watchParty);
            return "redirect:/watchparty";
        }
        else{
            model.addAttribute("user",userService.getCurrentUser());
            model.addAttribute("watchparty", watchParty);
            return "watchparty/create";
        }
    }

    @GetMapping("/{watchPartyId}/update")
    public String updateWatchPartyForm(@PathVariable Integer watchPartyId, Model model){
        model.addAttribute("user",userService.getCurrentUser());
        model.addAttribute("watchparty", watchPartyService.getWatchParty(watchPartyId));
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
        boolean success = watchPartyService.deleteWatchParty(watchPartyId, userService.getCurrentUser().getProfile());
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
    public String addMovieToWatchParty(@PathVariable Integer movieId, @RequestParam(name = "watchpartyOption", required = false) List<Integer> watchPartyIds){
        watchPartyService.addMovieToList(movieId, watchPartyIds);
        return "redirect:/movie-details/"+movieId;
    }

    @GetMapping("/{watchPartyId}/movies/remove/{movieId}")
    public String removeMovieFromWatchParty(@PathVariable Integer watchPartyId, @PathVariable Integer movieId){
        watchPartyService.removeMovie(watchPartyId, movieId);
        return "redirect:/watchparty/"+watchPartyId;
    }

    @PostMapping("/{watchPartyId}/members/remove")
    public String removeMemberFromWatchParty(@PathVariable Integer watchPartyId, @RequestParam("profileId") Integer profileId){
        watchPartyService.removeMember(watchPartyId, profileId, userService.getCurrentUser().getProfile());
        return "redirect:/watchparty/"+watchPartyId;
    }

    @PostMapping("/{watchPartyId}/votes/cast")
    public String castVote(@PathVariable Integer watchPartyId, @RequestParam("movieId") Integer movieId){
        voteService.castVote(watchPartyService.getWatchParty(watchPartyId), movieId, userService.getCurrentUser().getProfile());
        return "redirect:/watchparty/"+watchPartyId;
    }
}
