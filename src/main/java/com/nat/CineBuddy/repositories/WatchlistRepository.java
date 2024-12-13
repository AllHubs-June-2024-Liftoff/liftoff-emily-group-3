package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Watchlist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistRepository extends CrudRepository<Watchlist, Integer> {
    // Custom query method to fetch watchlists by user ID
    List<Watchlist> findByUserId(Integer userId);
}
