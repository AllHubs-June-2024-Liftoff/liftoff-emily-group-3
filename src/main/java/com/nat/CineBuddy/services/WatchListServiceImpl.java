package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.WatchList;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.repositories.WatchListRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WatchListServiceImpl implements WatchListService {

    @Autowired
    private WatchListRepository watchListRepository;
    @Autowired
    private TMDbService tmdbService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProfileService profileService;

    public boolean createWatchList(WatchList watchList) {
        watchList.setProfile(userService.getCurrentUser().getProfile());
        watchListRepository.save(watchList);
        return true;
    }

    @Override
    public List<WatchList> getWatchListsByUser(String username) {
        Profile profile = userService.getCurrentUser().getProfile();
        return watchListRepository.findByProfile(profile);
    }

    @Transactional
    public void addMovieToList(Integer movieId, List<Integer> watchListIds) {
        if (watchListIds != null && !watchListIds.isEmpty()) {
            for (Integer watchListId : watchListIds) {
                Optional<WatchList> storedWatchList = watchListRepository.findById(watchListId);
                if (storedWatchList.isPresent()) {
                    WatchList watchList = storedWatchList.get();
                    if (!watchList.getMovies().contains(movieId)) {
                        watchList.getMovies().add(movieId);
                        watchListRepository.save(watchList);
                    }

                }
            }
        }
    }


    public WatchList getWatchList(Integer id){
        return watchListRepository.findById(id).orElseGet(WatchList::new);
    }


    public boolean deleteWatchList(Integer watchListId){
        Optional<WatchList> storedWatchList = watchListRepository.findById(watchListId);
        if(!storedWatchList.isPresent()){
            return false;
        }
        else{
            WatchList watchListToRemove = storedWatchList.get();
                watchListRepository.delete(watchListToRemove);
                return true;
            }

        }

public void removeMovie(Integer watchListId, Integer movieId) {
    Optional<WatchList> storedWatchList = watchListRepository.findById(watchListId);
    if (storedWatchList.isPresent()) {
        WatchList watchList = storedWatchList.get();
        watchList.getMovies().remove(movieId);
        watchListRepository.save(watchList);
        }
    }
 }

