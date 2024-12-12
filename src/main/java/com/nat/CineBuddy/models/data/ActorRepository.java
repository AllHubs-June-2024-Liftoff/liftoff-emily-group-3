package com.nat.CineBuddy.models.data;

import com.nat.CineBuddy.models.Actor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends CrudRepository<Actor, Integer> {
}