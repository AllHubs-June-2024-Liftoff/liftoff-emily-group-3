package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Badge;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BadgeController {

    @GetMapping("/badges")
    public String showBadges(Model model) {
        List<Badge> badges = List.of(
                new Badge("First Review", "Awarded for submitting your first review."),
                new Badge("Hater", "Given for leaving 3 or more 1-star reviews."),
                new Badge("Binge Watcher", "Awarded for leaving 3 or more reviews in one day."),
                new Badge("Explorer", "Earned by reviewing movies from 5 or more different genres."),
                new Badge("Horror Enthusiast", "Awarded for leaving 3 or more 5-star reviews for horror movies.")
        );
        model.addAttribute("badges", badges);
        return "badges";
    }
}
