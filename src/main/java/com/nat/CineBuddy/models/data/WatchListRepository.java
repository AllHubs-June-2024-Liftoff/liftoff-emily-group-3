package com.nat.CineBuddy.models.data;

import com.nat.CineBuddy.models.WatchList;
import org.aspectj.apache.bcel.Repository;
import org.springframework.data.repository.CrudRepository;

public interface WatchListRepository extends CrudRepository<WatchList, Integer> {
}
