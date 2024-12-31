package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Actor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TMDbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private final RestTemplate restTemplate;

    public TMDbService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public List<MovieDTO> getTrendingMovies() {
        String url = BASE_URL + "/trending/movie/day?api_key=" + apiKey;
        MovieDTO[] trendingMoviesArray = restTemplate.getForObject(url, MovieDTO[].class);
        assert trendingMoviesArray != null;
        return List.of(trendingMoviesArray); // Convert the array to a list and return
    }
    // Fetch movie details and return MovieDTO
    public MovieDTO getMovieDetails(Integer movieId) {
        try {
            JsonNode response = fetchFromApi("/movie/" + movieId);
            return response != null ? parseMovieDTO(response) : null;
        } catch (Exception e) {
            System.err.println("Error fetching movie details: " + e.getMessage());
            return null;
        }
    }

    // Fetch similar movie recommendations and return a list of MovieDTOs
    public List<MovieDTO> getSimilarMovieRecommendations(Integer movieId) {
        try {
            JsonNode response = fetchFromApi("/movie/" + movieId + "/recommendations");
            return response != null && response.has("results") ? parseMovieDTOs(response.get("results")) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error fetching similar movie recommendations: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Fetch actors for a movie
    public List<Actor> getMovieActors(Integer movieId) {
        try {
            JsonNode response = fetchFromApi("/movie/" + movieId + "/credits");
            return response != null && response.has("cast") ? parseActors(response.get("cast")) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error fetching movie actors: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Helper method to fetch data from TMDb API
    private JsonNode fetchFromApi(String endpoint) {
        String url = BASE_URL + endpoint + "?api_key=" + apiKey;
        return restTemplate.getForObject(url, JsonNode.class);
    }

    // Parse movie data from TMDb API and return as MovieDTO
    private MovieDTO parseMovieDTO(JsonNode node) {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(node.get("id").asInt());
        movieDTO.setTitle(node.get("title").asText());
        movieDTO.setOverview(node.get("overview").asText());
        movieDTO.setReleaseDate(node.get("release_date").asText());
        movieDTO.setPosterPath(node.get("poster_path").asText());
        return movieDTO;
    }

    // Parse a list of MovieDTOs from a JsonNode array
    private List<MovieDTO> parseMovieDTOs(JsonNode nodes) {
        return StreamSupport.stream(nodes.spliterator(), false)
                .map(this::parseMovieDTO)
                .collect(Collectors.toList());
    }

    // Parse actor information
    private List<Actor> parseActors(JsonNode nodes) {
        return StreamSupport.stream(nodes.spliterator(), false)
                .map(node -> new Actor(node.get("name").asText(), node.get("character").asText()))
                .collect(Collectors.toList());
    }
}
