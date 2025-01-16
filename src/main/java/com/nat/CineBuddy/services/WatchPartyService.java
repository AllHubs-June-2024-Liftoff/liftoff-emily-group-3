package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.WatchParty;

import java.util.List;
import java.util.Optional;

public interface WatchPartyService {
    public boolean createWatchParty(WatchParty watchParty);
    public void addMovieToList(Integer movieId, List<Integer> watchPartyIds);
    public WatchParty viewWatchParty(Integer id);
    public boolean updateWatchParty(Integer watchPartyId, WatchParty watchParty);
    public boolean deleteWatchParty(Integer watchPartyId);
    public boolean leaveWatchParty(Integer watchPartyId, Profile removeProfile);
//    Optional<WatchParty> findById(Integer id);


}
