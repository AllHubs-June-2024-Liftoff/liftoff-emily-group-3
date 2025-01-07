package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.WatchParty;

public interface WatchPartyService {
    public boolean createWatchParty(WatchParty watchParty);
    public WatchParty viewWatchParty(Integer id);
    public boolean updateWatchParty(WatchParty watchParty);
    public boolean deleteWatchParty(Integer watchPartyId);
}
