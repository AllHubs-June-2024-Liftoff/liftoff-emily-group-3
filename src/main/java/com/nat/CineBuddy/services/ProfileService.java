package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Review;
import com.nat.CineBuddy.models.Role;
import com.nat.CineBuddy.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface ProfileService {

    boolean createProfile(User user, String name);

    boolean updateProfile(Integer profileId, Profile profile);

    Profile getProfileById(Integer id);

    Iterable<Profile> getAllProfiles();

    void deleteProfileById(Integer id);

    public void logoutUser(HttpServletRequest request, HttpServletResponse response);

    public Iterable<Profile> searchAllPublicProfiles(String search);

    public void saveAll(List<Profile> profiles);

    public List<MovieDTO> getTopRatedMovies(List<Review> reviews);
}
