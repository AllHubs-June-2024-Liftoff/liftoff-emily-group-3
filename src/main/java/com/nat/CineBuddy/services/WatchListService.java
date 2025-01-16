package com.nat.CineBuddy.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nat.CineBuddy.models.WatchList;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.repositories.WatchListRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WatchListService {

    private final WatchListRepository watchListRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON serialization/deserialization

    public WatchListService(WatchListRepository watchListRepository) {
        this.watchListRepository = watchListRepository;
    }

    public WatchList createWatchList(String name, Profile profile) {
        WatchList watchList = new WatchList(name, profile);
        return watchListRepository.save(watchList);
    }

    public Optional<WatchList> getWatchListById(Integer id) {
        return watchListRepository.findById(id);
    }

    public void deleteWatchList(Integer id) {
        watchListRepository.deleteById(id);
    }

    public WatchList save(WatchList watchList) {
        return watchListRepository.save(watchList);
    }

    public void addMovieToWatchList(WatchList watchList, MovieDTO movie) {
        try {
            String movieJson = objectMapper.writeValueAsString(movie);
            watchList.getMovies().add(movieJson);
            watchListRepository.save(watchList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize MovieDTO to JSON", e);
        }
    }

    public List<MovieDTO> getMoviesFromWatchList(WatchList watchList) {
        List<MovieDTO> movies = new ArrayList<>();
        for (String movieJson : watchList.getMovies()) {
            try {
                movies.add(objectMapper.readValue(movieJson, MovieDTO.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to deserialize JSON to MovieDTO", e);
            }
        }
        return movies;
    }
}
