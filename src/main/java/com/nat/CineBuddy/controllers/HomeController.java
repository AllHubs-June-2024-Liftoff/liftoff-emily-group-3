package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.services.TMDbService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final TMDbService tmDbService;

    public HomeController(TMDbService tmDbService) {
        this.tmDbService = tmDbService;
    }

    @GetMapping("/")
    public String trendingMovies(Model model) {
        List<MovieDTO> movies = tmDbService.getTrendingMovies();  // Use MovieDTO here
        List<MovieDTO> upcomingMovies = tmDbService.getUpcomingMovies();
        model.addAttribute("movies", movies);
        model.addAttribute("upcomingMovies", upcomingMovies);
        return "index";
    }
}
