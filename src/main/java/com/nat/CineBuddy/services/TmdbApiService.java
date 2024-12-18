//package com.nat.CineBuddy.services;
//
//import com.nat.CineBuddy.dto.MovieDto;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class TmdbApiService {
//
//    @Value("${tmdb.api.key}")
//    private String apiKey;
//
//    private final RestTemplate restTemplate;
//
//    public TmdbApiService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public List<MovieDto> searchMovies(String query) {
//        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + query;
//
//        ResponseEntity<TmdbMovieSearchResponse> response = restTemplate.getForEntity(url, TmdbMovieSearchResponse.class);
//        if (response.getBody() != null) {
//            return response.getBody().getResults();
//        }
//
//        return new ArrayList<>();
//    }
//
//    /**
//     * Fetch detailed information about a specific movie by its ID from the TMDB API.
//     *
//     * @param movieId The TMDB movie ID
//     * @return A MovieDto containing detailed information about the movie
//     */
//    public MovieDto getMovieDetails(Integer movieId) {
//        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
//
//        // Call the TMDB API to fetch movie details
//        ResponseEntity<MovieDto> response = restTemplate.getForEntity(url, MovieDto.class);
//
//        // Return the movie details if the response body is not null
//        if (response.getBody() != null) {
//            return response.getBody();
//        }
//
//        throw new IllegalArgumentException("Movie with ID " + movieId + " not found.");
//    }
//}
//
//
