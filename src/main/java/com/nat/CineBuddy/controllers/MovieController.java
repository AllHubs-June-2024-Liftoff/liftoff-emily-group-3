package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Actor;
import com.nat.CineBuddy.models.Movie;
import com.nat.CineBuddy.service.TMDbService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MovieController {
    private final TMDbService tmDbService;

    public MovieController(TMDbService tmDbService) {
        this.tmDbService = tmDbService;
    }

    @GetMapping("/movie-details/{id}")
    public String getMovieDetails(@PathVariable String id, Model model) {
        Movie movie = tmDbService.getMovieDetails(id);
        List<Movie> similarMovies = tmDbService.getSimilarMovieRecommendations(id);
        List<Actor> actors = tmDbService.getMovieActors(id);
        model.addAttribute("movie", movie);
        model.addAttribute("similarMovies", similarMovies);
        model.addAttribute("actors", actors);
        return "movies/movie-details";
    }

}
