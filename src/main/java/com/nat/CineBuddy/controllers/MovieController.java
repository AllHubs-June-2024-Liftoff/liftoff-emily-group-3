package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Actor;
import com.nat.CineBuddy.services.TMDbService;
import com.nat.CineBuddy.dto.MovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
public class MovieController {
    private final TMDbService tmDbService;

    @Autowired
    public MovieController(TMDbService tmDbService) {
        this.tmDbService = tmDbService;
    }



    @GetMapping("/movie-details/{id}")
    public String getMovieDetails(@PathVariable String id, Model model) {
        MovieDTO movieDTO = tmDbService.getMovieDetails(id);
        if (movieDTO == null) {
            return "redirect:/error";
        }

        List<MovieDTO> similarMovies = tmDbService.getSimilarMovieRecommendations(id);
        List<Actor> actors = tmDbService.getMovieActors(id);

        model.addAttribute("movie", movieDTO);
        model.addAttribute("similarMovies", similarMovies);
        model.addAttribute("actors", actors);

        return "movies/movie-details";
    }
}