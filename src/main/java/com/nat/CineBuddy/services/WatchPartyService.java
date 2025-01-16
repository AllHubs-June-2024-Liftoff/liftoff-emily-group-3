package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.WatchParty;

import java.util.List;

public interface WatchPartyService {
    public boolean createWatchParty(WatchParty watchParty);
    public void addMovieToList(Integer movieId, List<Integer> watchPartyIds);
    public WatchParty getWatchParty(Integer id);
    public boolean updateWatchParty(Integer watchPartyId, WatchParty watchParty);
    public boolean deleteWatchParty(Integer watchPartyId);
    public boolean leaveWatchParty(Integer watchPartyId, Profile removeProfile);
    public void removeMovie(Integer watchPartyId, Integer movieId);
    public void removeMember(Integer watchPartyId, Integer memberId);
    public List<MovieDTO> getTopRatedMovies(WatchParty watchParty);
}
