package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Role;
import com.nat.CineBuddy.models.User;

public interface ProfileService {

    boolean createProfile(String name, User user);

    boolean updateProfile(Integer profileId, Profile profile);

    Profile findByName(String name);

    Profile getProfileById(Integer id);

    Iterable<Profile> getAllProfiles();

    void deleteProfileById(Integer id);

}
