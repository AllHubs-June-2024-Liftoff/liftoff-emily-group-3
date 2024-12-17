package com.nat.CineBuddy.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

@Service
public class TMDbService {

    // Inject your TMDb API base URL and API key from application.properties
    @Value("${tmdb.api.base-url}")
    private String tmdbBaseUrl;

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    // Constructor for injecting RestTemplate
    public TMDbService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Example method to fetch movie details by TMDb ID
    public String getMovieById(int movieId) {
        // Construct the URL for the TMDb API request
        String url = String.format("%s/movie/%d?api_key=%s", tmdbBaseUrl, movieId, apiKey);

        // Use RestTemplate to make the GET request
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Return the response body (the movie data)
        return response.getBody();
    }

    // Add more methods for other endpoints as needed
}
