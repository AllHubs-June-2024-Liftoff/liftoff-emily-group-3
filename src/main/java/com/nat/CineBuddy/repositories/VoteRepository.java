package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.models.WatchParty;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRepository extends CrudRepository<Vote, Integer> {
    List<Vote> findByWatchParty(WatchParty watchParty); // Find all votes for a specific WatchParty
}
