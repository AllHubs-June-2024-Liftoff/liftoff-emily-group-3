package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Actor;
import com.nat.CineBuddy.models.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    // Fetch movie details
    public Movie getMovieDetails(String movieId) {
        try {
            JsonNode response = fetchFromApi("/movie/" + movieId);
            return response != null ? parseMovie(response) : null;
        } catch (Exception e) {
            System.err.println("Error fetching movie details: " + e.getMessage());
            return null;
        }
    }

    // Fetch trending movies
    public List<Movie> getTrendingMovies() {
        try {
            JsonNode response = fetchFromApi("/trending/movie/day");
            return response != null && response.has("results") ? parseMovies(response.get("results")) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error fetching trending movies: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Fetch similar movie recommendations
    public List<Movie> getSimilarMovieRecommendations(String movieId) {
        try {
            JsonNode response = fetchFromApi("/movie/" + movieId + "/recommendations");
            return response != null && response.has("results") ? parseMovies(response.get("results")) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error fetching similar movie recommendations: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Fetch actors for a movie
    //todo rename e variable to error
    public List<Actor> getMovieActors(String movieId) {
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

    // Parse a single movie JSON object into a Movie instance
    private Movie parseMovie(JsonNode node) {
        String id = getJsonField(node, "id", "N/A");
        String title = getJsonField(node, "title", "Unknown Title");
        String overview = getJsonField(node, "overview", "No overview available");
        String releaseDate = getJsonField(node, "release_date", "N/A");
        String posterPath = getJsonField(node, "poster_path", "N/A");
        String genres = parseGenres(node);
        String budget = getJsonField(node, "budget", "N/A");
        String revenue = getJsonField(node, "revenue", "N/A");
        String runtime = getJsonField(node, "runtime", "N/A");
        String voteAverage = getJsonField(node, "vote_average", "N/A");

        return new Movie(id, title, overview, releaseDate, posterPath, genres, budget, revenue, runtime, voteAverage);
    }

    // Parse a list of movie JSON objects into a list of Movie instances
    private List<Movie> parseMovies(JsonNode nodes) {
        return StreamSupport.stream(nodes.spliterator(), false)
                .map(this::parseMovie)
                .collect(Collectors.toList());
    }

    // Parse a list of actor JSON objects into a list of Actor instances
    private List<Actor> parseActors(JsonNode nodes) {
        return StreamSupport.stream(nodes.spliterator(), false)
                .map(node -> new Actor(
                        getJsonField(node, "id", "N/A"),
                        getJsonField(node, "name", "Unknown Actor"),
                        getJsonField(node, "character", "Unknown Character"),
                        getJsonField(node, "profile_path", null)
                ))
                .collect(Collectors.toList());
    }

    // Parse genres from a movie JSON object
    private String parseGenres(JsonNode node) {
        if (!node.has("genres")) return "N/A";
        return StreamSupport.stream(node.get("genres").spliterator(), false)
                .map(genre -> genre.get("name").asText())
                .collect(Collectors.joining(", "));
    }

    // Helper method to safely extract a JSON field
    private String getJsonField(JsonNode node, String fieldName, String defaultValue) {
        return node.has(fieldName) ? node.get(fieldName).asText() : defaultValue;
    }

    /**
     * - Searches for movies using an external API.
     * - Takes a search query, sends a GET request to the API, and retrieves results.
     * - Converts the API response into a list of Movie objects.
     * - Returns the list of movies or an empty list if no results or errors occur.
     */
    public List<Movie> searchMovies(String query) {
        try {
            String url = BASE_URL + "/search/movie?query=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&api_key=" + apiKey;
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response != null && response.has("results")) {
                return parseMovies(response.get("results"));
            }
        } catch (Exception e) {
            System.err.println("Error searching for movies: " + e.getMessage());
        }
        return Collections.emptyList();
    }


}
