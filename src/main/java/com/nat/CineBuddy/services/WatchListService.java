package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.WatchList;

import java.util.List;

public interface WatchListService {
    public boolean createWatchList(WatchList watchList);
    public void addMovieToList(Integer movieId, List<Integer> watchListIds);
    public WatchList getWatchList (Integer id);
    public boolean deleteWatchList(Integer watchListId);
    public void removeMovie(Integer watchListId, Integer movieId);
    List<WatchList> getWatchListsByProfile();

}
