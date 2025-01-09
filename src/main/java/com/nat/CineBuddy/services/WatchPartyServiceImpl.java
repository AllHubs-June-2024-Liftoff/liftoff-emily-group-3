package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.repositories.WatchPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WatchPartyServiceImpl implements WatchPartyService{

    @Autowired
    private WatchPartyRepository watchPartyRepository;
    @Autowired
    private ProfileService profileService;

    public boolean createWatchParty(WatchParty watchParty){
        watchPartyRepository.save(watchParty);
        return true;
    }

    public void addMovieToList(Integer movieId, List<Integer> watchPartyIds){
        for (Integer watchPartyId : watchPartyIds){
            Optional<WatchParty> storedWatchParty = watchPartyRepository.findById(watchPartyId);
            if(storedWatchParty.isPresent()){
                WatchParty watchParty = storedWatchParty.get();
                if(!watchParty.getMovies().contains(movieId)){
                    watchParty.getMovies().add(movieId);
                    watchPartyRepository.save(watchParty);
                }
            }
        }
    }

    public WatchParty viewWatchParty(Integer id){
        return watchPartyRepository.findById(id).orElseGet(WatchParty::new);
    }

    public boolean updateWatchParty(Integer watchPartyId, WatchParty watchParty){
        Optional<WatchParty> storedWatchParty = watchPartyRepository.findById(watchPartyId);
        if(!storedWatchParty.isPresent()){
            return false;
        }
        else{
            WatchParty updatedWatchParty = storedWatchParty.get();
            updatedWatchParty.setName(watchParty.getName());
            updatedWatchParty.setMovies(watchParty.getMovies());
            updatedWatchParty.setMovieChoice(watchParty.getMovieChoice());
            updatedWatchParty.setMembers(watchParty.getMembers());
            watchPartyRepository.save(updatedWatchParty);
            return true;
        }
    }

    public boolean deleteWatchParty(Integer watchPartyId){
        Optional<WatchParty> storedWatchParty = watchPartyRepository.findById(watchPartyId);
        if(!storedWatchParty.isPresent()){
            return false;
        }
        else{
            WatchParty watchPartyToRemove = storedWatchParty.get();
            for (Profile memberToRemove : watchPartyToRemove.getMembers()){
                memberToRemove.getJoinedGroups().remove(watchPartyToRemove);
                profileService.updateProfile(memberToRemove.getId(), memberToRemove);
            }
            watchPartyRepository.delete(watchPartyToRemove);
            return true;
        }
    }

    public boolean leaveWatchParty(Integer watchPartyId, Profile removeProfile){
        Optional<WatchParty> storedWatchParty = watchPartyRepository.findById(watchPartyId);
        if(!storedWatchParty.isPresent()){
            return false;
        }
        else{
            WatchParty watchPartyToRemove = storedWatchParty.get();
            watchPartyToRemove.getMembers().remove(removeProfile);
            removeProfile.getJoinedGroups().remove(watchPartyToRemove);
            watchPartyRepository.save(watchPartyToRemove);
            profileService.updateProfile(removeProfile.getId(),removeProfile);
            return true;
        }

    }

    @Override
    public Optional<WatchParty> findById(Integer id) {
        return watchPartyRepository.findById(id);
    }


}