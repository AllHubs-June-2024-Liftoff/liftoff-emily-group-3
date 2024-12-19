package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.services.ProfileService;
import com.nat.CineBuddy.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("profile/update")
    public String userProfileUpdateForm(@AuthenticationPrincipal User currentUser, Model model){
        model.addAttribute("user",userService.getCurrentUser());
        return "user/update";
    }

    @PostMapping("profile/update")
    public String userProfileUpdate(@AuthenticationPrincipal User currentUser, @Valid @ModelAttribute("user") com.nat.CineBuddy.models.User user, BindingResult result, Errors errors, Model model){
        System.out.println(user);
        if(!errors.hasErrors() && !result.hasErrors()){
            model.addAttribute("user",userService.getCurrentUser());
            return "user/index";
        }
        else{
            model.addAttribute("user",user);
            return "user/update";
        }
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
