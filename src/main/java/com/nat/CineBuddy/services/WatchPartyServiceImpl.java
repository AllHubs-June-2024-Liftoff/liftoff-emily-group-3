package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.repositories.WatchPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchPartyServiceImpl implements WatchPartyService{

    @Autowired
    private WatchPartyRepository watchPartyRepository;
    @Autowired
    private ProfileService profileService;

    public boolean createWatchParty(WatchParty watchParty){
        watchPartyRepository.save(watchParty);
        for (Profile member : watchParty.getMembers()){
            member.getJoinedGroups().add(watchParty);
            profileService.updateProfile(member.getId(),member);
        }
        return true;
    }

    public WatchParty viewWatchParty(Integer id){
        return watchPartyRepository.findById(id).orElseGet(WatchParty::new);
    }

    public boolean updateWatchParty(WatchParty watchParty){
        Optional<WatchParty> storedWatchParty = watchPartyRepository.findById(watchParty.getId());
        if(!storedWatchParty.isPresent()){
            return false;
        }
        else{
            WatchParty updatedWatchParty = storedWatchParty.get();
            updatedWatchParty.setName(watchParty.getName());
            updatedWatchParty.setMembers(watchParty.getMembers());
            updatedWatchParty.setMovies(watchParty.getMovies());
            updatedWatchParty.setMovieChoice(watchParty.getMovieChoice());
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

}
