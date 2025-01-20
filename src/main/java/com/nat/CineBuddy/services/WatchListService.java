package com.nat.CineBuddy.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nat.CineBuddy.models.WatchList;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.repositories.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WatchListService {

    private final WatchListRepository watchListRepository;
    private final TMDbService tmdbService;

    @Autowired
    public WatchListService(WatchListRepository watchListRepository, TMDbService tmdbService) {
        this.watchListRepository = watchListRepository;
        this.tmdbService = tmdbService;
    }

    public WatchList createWatchList(String name, Profile profile) {
        WatchList watchList = new WatchList(name, profile);
        return watchListRepository.save(watchList);
    }

    public Optional<WatchList> getWatchListById(Integer id) {
        return id == null ? Optional.empty() : watchListRepository.findById(id);
    }

    public void deleteWatchList(Integer id) {
        watchListRepository.deleteById(id);
    }

    public void addMovieToWatchList(WatchList watchList, String movieId) {
        if (!watchList.getMovieIds().contains(movieId)) {
            watchList.getMovieIds().add(movieId);
            watchListRepository.save(watchList);
        }
    }

    public void removeMovieFromWatchList(WatchList watchList, String movieId) {
        if (watchList.getMovieIds().contains(movieId)) {
            watchList.getMovieIds().remove(movieId);
            watchListRepository.save(watchList);
        }
    }

    public List<MovieDTO> getMoviesFromWatchList(WatchList watchList) {
        return watchList.getMovieIds().stream()
                .map(tmdbService::getMovieDetails)
                .collect(Collectors.toList());
    }

    public List<WatchList> getWatchListsByProfile(Profile profile) {
        return watchListRepository.findByProfile(profile);
    }

}


