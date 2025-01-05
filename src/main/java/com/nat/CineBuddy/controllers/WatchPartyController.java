package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.services.ProfileService;
import com.nat.CineBuddy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/watchparty")
public class WatchPartyController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

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
}
