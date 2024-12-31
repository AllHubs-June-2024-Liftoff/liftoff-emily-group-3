//package com.nat.CineBuddy.services;
//
//import com.nat.CineBuddy.dto.MovieDTO;
//import com.nat.CineBuddy.models.WatchList;
//import com.nat.CineBuddy.repositories.WatchListRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class WatchListService {
//
//    @Autowired
//    private WatchListRepository watchListRepository;
//
//
//    @Autowired
//    private TMDbService tmdbService;
//
//    public void addMovieToWatchList(Integer userId, Integer movieId) {
//        WatchList watchList = new WatchList();
//        watchList.setUserId(userId);
//        watchList.setMovieId(movieId);
//        watchListRepository.save(watchList);
//    }
//
//    public List<MovieDTO> getWatchListForUser(Integer userId) {
//        List<WatchList> watchLists = watchListRepository.findByUserId(userId);
//        List<MovieDTO> movieDetails = new ArrayList<>();
//
//        for (WatchList item : watchLists) {
//            // Call the TMDb API to fetch the movie details
//            MovieDTO movie = TMDbService.getMovieById(item.getMovieId());
//            movieDetails.add(movie);
//        }
//
//        return movieDetails;
//    }
//}
