package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.services.TMDbService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final TMDbService tmDbService;

    public SearchController(TMDbService tmDbService) {
        this.tmDbService = tmDbService;
    }

    @GetMapping("/search")
    public String searchResults(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "searchBy", required = false, defaultValue = "title") String searchBy,
            @RequestParam(value = "sortBy", required = false, defaultValue = "relevance") String sortBy,
            @RequestParam(value = "genre", required = false) String genre,
            Model model) {

        // If no query and no genre selected, return empty results
        if ((query == null || query.isBlank()) && (genre == null || genre.isBlank())) {
            model.addAttribute("movies", List.of());
            return "search/search-results";
        }

        // Allow searching by genre only, without requiring a query
        List<MovieDTO> movies;
        if (genre != null && !genre.isEmpty() && (query == null || query.isBlank())) {
            movies = tmDbService.searchMoviesByGenre(genre, sortBy);
        } else {
            movies = tmDbService.searchMovies(query, searchBy, sortBy, genre);
        }

        model.addAttribute("movies", movies);
        return "search/search-results";
    }


    @GetMapping("/search-menu")
    public String searchMenu(Model model) {
        model.addAttribute("genres", tmDbService.getGenres());
        return "search/search-menu";
    }
}
