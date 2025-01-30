package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Badge;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.repositories.ReviewRepository;
import com.nat.CineBuddy.security.CBUserDetailsService;
import com.nat.CineBuddy.services.BadgeService;
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

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CBUserDetailsService cbUserDetailsService;

    @GetMapping("profile")
    public String index(Model model){
        List<Review> reviews = reviewRepository.findByProfileIdOrderByRatingDesc(userService.getCurrentUser().getProfile().getId());
        List<Review> userReviews = reviewRepository.findByProfileId(userService.getCurrentUser().getProfile().getId());
        List<Badge> badges = badgeService.getUserBadges(userService.getCurrentUser().getProfile().getId());

        List<Review> sortedReviews = reviews.stream()
                .sorted(Comparator.comparing(Review::getDateCreated).reversed())
                .collect(Collectors.toList());
        model.addAttribute("sortedReviews", sortedReviews);

        model.addAttribute("user",userService.getCurrentUser());
        model.addAttribute("topRated",profileService.getTopRatedMovies(reviews.subList(0, Math.min(10, reviews.size()))));
        model.addAttribute("badges", badges);
        model.addAttribute("reviews", userReviews);
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

    @GetMapping("profile/account")
    public String userAccountUpdateForm(Model model){
        User Storeduser = userService.getCurrentUser();
        Storeduser.setPassword("");
        model.addAttribute("user",Storeduser);
        return "profile/account";
    }

    @PostMapping("profile/account")
    public String userAccountUpdate(@Valid @ModelAttribute("user") User user, BindingResult result, Errors errors, Model model){
        boolean success = true;
        com.nat.CineBuddy.models.User updatedUser = userService.getCurrentUser();
        if(!errors.hasErrors() && !result.hasErrors()) {
            if(!userService.areFieldsUnique(user.getUsername()) && !updatedUser.getUsername().equals(user.getUsername())){
                errors.rejectValue("username","username.notUnique", "Username is already taken.");
                success = false;
            }
            if(!userService.areFieldsUnique(user.getEmail()) && !updatedUser.getEmail().equals(user.getEmail())){
                errors.rejectValue("email","email.notUnique", "Email is already taken.");
                success = false;
            }
        }
        else{
            success = false;
        }
        if(success){
            userService.updateUser(user, updatedUser.getId());
            cbUserDetailsService.updateAuth(user.getUsername());
            User Storeduser = userService.getCurrentUser();
            Storeduser.setPassword("");
            model.addAttribute("user",Storeduser);
            model.addAttribute("updatePass", "Update successful!");
            return "profile/account";
        }
        else{
            model.addAttribute("user", user);
            model.addAttribute("updateFail", "Update failed. Please try again.");
            return "profile/account";
        }
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
