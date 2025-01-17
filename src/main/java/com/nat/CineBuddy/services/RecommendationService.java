package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.RecommendedMovie;
import com.nat.CineBuddy.repositories.RecommendedMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    @Autowired
    private TMDbService tmDbService;

    @Autowired
    private RecommendedMovieRepository recommendedMovieRepository;

    @Autowired
    private UserService userService;

    /**
     * Update recommendations when a user adds a movie to their watchlist or reviews a movie.
     */
    public void updateRecommendations(String movieId){
        //Get the current users profile
        Profile profile = userService.getCurrentUser().getProfile();

        //Getting movies using TMdbService.
        List<MovieDTO> recommendedMovies = tmDbService.getSimilarMovieRecommendations(movieId);

        for(MovieDTO movie : recommendedMovies){
            //Check if movie already exists in the users recommendations.
            if(!recommendedMovieRepository.existsByProfileAndMovieId(profile, movie.getId())){

                //Add the movie to the recommendations.
                RecommendedMovie recommendedMovie = new RecommendedMovie();
                recommendedMovie.setProfile(profile);
                recommendedMovie.setMovieId(movie.getId());
                recommendedMovie.setTitle(movie.getTitle());
                recommendedMovie.setPosterPath(movie.getPosterPath());
                recommendedMovieRepository.save(recommendedMovie);
            }
        }
    }

    //Fetch all recommended movies for the current user.

    public List<RecommendedMovie> getRecommendationsForCurrentUser(){
        Profile profile = userService.getCurrentUser().getProfile();
        return recommendedMovieRepository.findByProfile(profile);
    }
}
