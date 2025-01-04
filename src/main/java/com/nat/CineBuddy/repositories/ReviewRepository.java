package com.nat.CineBuddy.repositories;

import com.nat.CineBuddy.models.Movie;
import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    List<Review> findByUser(User user);
//    List<Review> findByMovieId(Integer id);
}
