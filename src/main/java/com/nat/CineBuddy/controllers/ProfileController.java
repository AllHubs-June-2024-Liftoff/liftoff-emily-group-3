package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.services.ProfileService;
import com.nat.CineBuddy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

}
