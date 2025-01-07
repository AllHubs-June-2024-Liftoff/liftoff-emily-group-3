package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.services.ProfileService;
import com.nat.CineBuddy.services.UserService;
import com.nat.CineBuddy.services.WatchPartyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping
    public String index(Model model){
        model.addAttribute("user",userService.getCurrentUser());
        return "watchparty/index";
    }

    @GetMapping("/{watchPartyId}")
    public String viewWatchParty(@PathVariable Integer watchPartyId, Model model){
        model.addAttribute("watchparty",watchPartyService.viewWatchParty(watchPartyId));
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
}
