package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Role;
import com.nat.CineBuddy.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ProfileService {

    boolean createProfile(User user, String name);

    boolean updateProfile(Integer profileId, Profile profile);

    Profile getProfileById(Integer id);

    Iterable<Profile> getAllProfiles();

    void deleteProfileById(Integer id);

    public void logoutUser(HttpServletRequest request, HttpServletResponse response);

    public Iterable<Profile> getAllPublicProfiles(String search);

}
