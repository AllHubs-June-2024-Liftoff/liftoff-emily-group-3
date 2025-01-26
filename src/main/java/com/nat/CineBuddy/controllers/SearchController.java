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
            Model model) {

        if (query == null || query.isEmpty()) {
            model.addAttribute("movies", List.of()); // Empty results
            return "search/search-results";
        }

        List<MovieDTO> movies = tmDbService.searchMovies(query, searchBy, sortBy);
        model.addAttribute("movies", movies);
        return "search/search-results";
    }
}
