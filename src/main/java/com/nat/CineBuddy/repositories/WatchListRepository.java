package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.WatchList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchListRepository extends CrudRepository<WatchList, Integer> {
    List<WatchList> findByUserId(Integer userId);
}

