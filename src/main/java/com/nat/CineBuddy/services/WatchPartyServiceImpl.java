package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.repositories.WatchPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
