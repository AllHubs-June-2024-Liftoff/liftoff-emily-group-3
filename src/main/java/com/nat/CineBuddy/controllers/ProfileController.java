package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.services.ProfileService;
import com.nat.CineBuddy.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String index(Model model){
        model.addAttribute("user",userService.getCurrentUser());
        return "profile/index";
    }

    @GetMapping("profile/update")
    public String userProfileUpdateForm(Model model){
        model.addAttribute("profile",userService.getCurrentUser().getProfile());
        return "profile/update";
    }

    @PostMapping("profile/update")
    public String userProfileUpdate(@Valid @ModelAttribute("profile") Profile profile, BindingResult result, Errors errors){
        if(!errors.hasErrors() && !result.hasErrors()){
            com.nat.CineBuddy.models.User updatedUser = userService.getCurrentUser();
            boolean success = profileService.updateProfile(updatedUser.getProfile().getId(),profile);
            if(success){
                return "redirect:/profile";
            }
            else{
                return "redirect:/profile/update";
            }
        }
        else{
            return "redirect:/profile/update";
        }
    }

    @GetMapping("profile/delete")
    public String deleteActiveUser(HttpServletRequest request, HttpServletResponse response){
        com.nat.CineBuddy.models.User deleteUser = userService.getCurrentUser();
        profileService.logoutUser(request,response);
        userService.deleteUserById(deleteUser.getId());
        return "redirect:/";
    }

    @GetMapping("profiles/{userName}")
    public String index(@PathVariable String userName, Model model){
        Optional<com.nat.CineBuddy.models.User> possibleUser = userService.findByUsername(userName);
        if(possibleUser.isPresent()){
            com.nat.CineBuddy.models.User searchedUser = possibleUser.get();
            if(!searchedUser.getProfile().getHidden()){
                model.addAttribute("user",searchedUser);
                return "profile/index";
            }
            else{
                model.addAttribute("search",userName);
                return "profile/notFound";
            }
        }
        else{
            model.addAttribute("search",userName);
            return "profile/notFound";
        }
    }

}
