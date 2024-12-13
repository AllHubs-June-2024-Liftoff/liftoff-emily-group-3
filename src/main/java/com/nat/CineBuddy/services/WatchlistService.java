package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Watchlist;
import com.nat.CineBuddy.repositories.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
Written by: Cody Adams
This service class handles the business logic for Watchlist operations.
It communicates with the WatchlistRepository to perform database interactions.
Allows for CRUD functionality with .save , findByUserId, findById.orElse, and deleteById.
*/

@Service
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;

    //Saves a new or updated wachlist to the database.

    public Watchlist save(Watchlist watchlist) {
        return watchlistRepository.save(watchlist);
    }

    //Finds Watchlists belonging to a specific user by their ID.

    public List<Watchlist> findByUserId(Integer userId) {
        return watchlistRepository.findByUserId(userId);
    }

    //Retrieves watchlist by its unique ID.

    public Watchlist findById(Integer id) {
        return watchlistRepository.findById(id).orElse(null);
    }

    //Deletes a Watchlist from the database by its unique ID.

    public void deleteById(Integer id) {
        watchlistRepository.deleteById(id);
    }
}
