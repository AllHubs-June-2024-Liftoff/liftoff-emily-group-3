package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.services.ProfileService;
import com.nat.CineBuddy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @GetMapping("profile")
    public String index(@AuthenticationPrincipal User currentUser, Model model){
        model.addAttribute("user",userService.getCurrentUser());
        return "user/index";
    }

    @GetMapping("profiles/{userName}")
    public String index(@PathVariable String userName, Model model){
        Optional<com.nat.CineBuddy.models.User> possibleUser = userService.findByUsername(userName);
        if(possibleUser.isPresent()){
            com.nat.CineBuddy.models.User searchedUser = possibleUser.get();
            if(!searchedUser.getProfile().isPrivate()){
                model.addAttribute("user",searchedUser);
                return "user/index";
            }
            else{
                model.addAttribute("search",userName);
                return "user/notFound";
            }
        }
        else{
            model.addAttribute("search",userName);
            return "user/notFound";
        }
    }

}
