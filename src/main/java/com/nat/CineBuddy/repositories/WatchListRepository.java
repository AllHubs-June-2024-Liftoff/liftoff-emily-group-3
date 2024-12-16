package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.WatchList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchListRepository extends CrudRepository<WatchList, Integer> {
}
