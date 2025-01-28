package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Movie;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.RecommendedMovie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendedMovieRepository extends CrudRepository<RecommendedMovie, Integer> {

    List<RecommendedMovie> findByProfile(Profile profile);
    boolean existsByProfileAndMovieId(Profile profile, String movieId);


}
