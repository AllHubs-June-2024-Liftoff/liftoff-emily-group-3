
package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Actor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
    public MovieDTO getMovieDetails(String movieId) {
        try {
            JsonNode response = fetchFromApi("/movie/" + movieId);
            return response != null ? parseMovie(response) : null;
        } catch (Exception e) {
            System.err.println("Error fetching movie details: " + e.getMessage());
            return null;
        }
    }

    // Fetch trending movies
    public List<MovieDTO> getTrendingMovies() {
        try {
            JsonNode response = fetchFromApi("/trending/movie/day");
            return response != null && response.has("results") ? parseMovies(response.get("results")) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error fetching trending movies: " + e.getMessage());
            return Collections.emptyList();
        }
    }


    // Fetch similar movie recommendations
    public List<MovieDTO> getSimilarMovieRecommendations(String movieId) {
        try {
            JsonNode response = fetchFromApi("/movie/" + movieId + "/recommendations");
            return response != null && response.has("results") ? parseMovies(response.get("results")) : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error fetching similar movie recommendations: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Fetch actors for a movie
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
    private MovieDTO parseMovie(JsonNode node) {
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

        return new MovieDTO(id, title, overview, releaseDate, posterPath, genres, budget, revenue, runtime, voteAverage);
    }

    // Parse a list of movie JSON objects into a list of Movie instances
    private List<MovieDTO> parseMovies(JsonNode nodes) {
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
    public List<MovieDTO> searchMovies(String query, String searchBy, String sortBy, String genre) {
        String endpoint = switch (searchBy.toLowerCase()) {
            case "actor" -> "/search/person";
            default -> "/search/movie";
        };

        String url = BASE_URL + endpoint + "?api_key=" + apiKey + "&query=" + query;

        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            List<MovieDTO> movies = new ArrayList<>();

            // Handle actor search using parseActorMovies
            if ("actor".equalsIgnoreCase(searchBy) && response != null && response.has("results")) {
                movies = parseActorMovies(response.get("results"));
            }

            // Handle other searches
            if (!"actor".equalsIgnoreCase(searchBy) && response != null && response.has("results")) {
                movies = parseMovies(response.get("results"));

                // Sort by top-rated if applicable
                if ("top-rated".equalsIgnoreCase(sortBy)) {
                    movies.sort((m1, m2) -> Double.compare(
                            Double.parseDouble(m2.getVoteAverage()),
                            Double.parseDouble(m1.getVoteAverage())
                    ));
                }
            }

            return movies;

        } catch (Exception e) {
            System.err.println("Error searching movies: " + e.getMessage());
            return Collections.emptyList();
        }
    }



    public Map<Integer, String> getGenres() {
        String url = BASE_URL + "/genre/movie/list?api_key=" + apiKey;
        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            Map<Integer, String> genres = new HashMap<>();
            if (response != null && response.has("genres")) {
                for (JsonNode genreNode : response.get("genres")) {
                    genres.put(genreNode.get("id").asInt(), genreNode.get("name").asText());
                }
            }
            return genres;
        } catch (Exception e) {
            System.err.println("Error fetching genres: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    public List<MovieDTO> searchMoviesByGenre(String genre, String sortBy) {
        String url = BASE_URL + "/discover/movie?api_key=" + apiKey + "&with_genres=" + genre;

        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            List<MovieDTO> movies = response != null && response.has("results")
                    ? parseMovies(response.get("results"))
                    : Collections.emptyList();

            if ("top-rated".equalsIgnoreCase(sortBy)) {
                movies.sort((m1, m2) -> Double.compare(Double.parseDouble(m2.getVoteAverage()), Double.parseDouble(m1.getVoteAverage())));
            }

            return movies;

        } catch (Exception e) {
            System.err.println("Error searching movies by genre: " + e.getMessage());
            return Collections.emptyList();
        }
    }



    private List<MovieDTO> parseActorMovies(JsonNode nodes) {
        return StreamSupport.stream(nodes.spliterator(), false)
                .flatMap(node -> node.has("known_for") ? parseMovies(node.get("known_for")).stream() : Stream.empty())
                .distinct()
                .collect(Collectors.toList());
    }
}