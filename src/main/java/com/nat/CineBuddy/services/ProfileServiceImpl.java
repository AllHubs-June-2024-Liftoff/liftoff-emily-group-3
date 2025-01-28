package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.repositories.ProfileRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProfileServiceImpl implements ProfileService{

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private TMDbService tmDbService;
    @Autowired
    private RecommendationService recommendationService;

    @Override
    public List<MovieDTO> getRecommendedMovies(Profile profile) {
        // Delegate to the RecommendationService to get recommendations
        return recommendationService.getRecommendationsBasedOnWatchlistAndReviews(profile);
    }

    public boolean createProfile(User user,String name){
        Optional<Profile> optionalProfile = profileRepository.findByUser(user);
        if(optionalProfile.isPresent()){
            return false;
        }
        Profile profile = new Profile();
        profile.setImage("/images/profile_thumb.png");
        profile.setUser(user);
        profile.setHidden(false);
        profile.setName(name);
        profileRepository.save(profile);
        return true;
    }

    public boolean updateProfile(Integer profileId, Profile profile){
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        if(!optionalProfile.isPresent()){
            return false;
        }
        Profile storedProfile = optionalProfile.get();
        storedProfile.setName(profile.getName());
        storedProfile.setBio(profile.getBio());
        storedProfile.setImage(profile.getImage());
        storedProfile.setHidden(profile.getHidden());
        storedProfile.setJoinedGroups(profile.getJoinedGroups());
        profileRepository.save(storedProfile);
        return true;
    }

    public Profile getProfileById(Integer id){
        return profileRepository.findById(id).orElseGet(Profile::new);
    }

    public Iterable<Profile> getAllProfiles(){
        return profileRepository.findAll();
    }

    public void deleteProfileById(Integer id){
        profileRepository.deleteById(id);
    }

    public Iterable<Profile> searchAllPublicProfiles(String search){
        Iterable<Profile> allProfiles = profileRepository.findByNameContainingIgnoreCase(search);
        Iterator<Profile> iterator = allProfiles.iterator();
        while(iterator.hasNext()){
            Profile profile = iterator.next();
            if(profile.getHidden()){
                iterator.remove();
            }
        }
        return allProfiles;
    }

    public void saveAll(List<Profile> profiles){
        profileRepository.saveAll(profiles);
    }

    public List<MovieDTO> getTopRatedMovies(List<Review> reviews){
        Set<MovieDTO> topRatedMovies = new HashSet<>();
        for(Review review : reviews){
            MovieDTO tempMovie = tmDbService.getMovieDetails(review.getMovieId());
            if(tempMovie != null){
                topRatedMovies.add(tempMovie);
            }
        }
        return new ArrayList<>(topRatedMovies);
    }

    public void logoutUser(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, response, authentication);  // Log out the user
        }
    }

}
