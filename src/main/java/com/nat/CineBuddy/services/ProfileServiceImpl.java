package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.repositories.ProfileRepository;
import com.nat.CineBuddy.security.CBUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService{

    @Autowired
    private ProfileRepository profileRepository;

    public boolean createProfile(User user){
        Optional<Profile> optionalProfile = profileRepository.findByUser(user);
        if(optionalProfile.isPresent()){
            return false;
        }
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setPrivate(false);
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
        storedProfile.setPrivate(profile.isPrivate());
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


}
