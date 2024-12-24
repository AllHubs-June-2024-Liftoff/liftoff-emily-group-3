package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Vote;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, Integer> {
}
