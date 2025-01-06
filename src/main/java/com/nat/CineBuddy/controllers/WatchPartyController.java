package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.services.ProfileService;
import com.nat.CineBuddy.services.UserService;
import com.nat.CineBuddy.services.WatchPartyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
            watchPartyService.createWatchParty(watchParty);
            return "redirect:/watchparty";
        }
        else{
            return "redirect:/watchparty/host";
        }
    }
}
