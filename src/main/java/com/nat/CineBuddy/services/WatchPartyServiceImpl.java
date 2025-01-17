package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.repositories.WatchPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WatchPartyServiceImpl implements WatchPartyService{

    @Autowired
    private WatchPartyRepository watchPartyRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserService userService;
    @Autowired
    private VoteService voteService;


    public boolean createWatchParty(WatchParty watchParty){
        watchPartyRepository.save(watchParty);
        return true;
    }

    public void addMovieToList(Integer movieId, List<Integer> watchPartyIds){
        if(watchPartyIds != null && !watchPartyIds.isEmpty()) {
            for (Integer watchPartyId : watchPartyIds) {
                Optional<WatchParty> storedWatchParty = watchPartyRepository.findById(watchPartyId);
                if (storedWatchParty.isPresent()) {
                    WatchParty watchParty = storedWatchParty.get();
                    if (!watchParty.getMovies().contains(movieId)) {
                        watchParty.getMovies().add(movieId);
                        watchPartyRepository.save(watchParty);
                    }
                }
            }
        }
    }

    public WatchParty getWatchParty(Integer id){
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
            if(watchPartyToRemove.getLeader().equals(userService.getCurrentUser().getProfile())){
                for (Profile memberToRemove : watchPartyToRemove.getMembers()){
                    memberToRemove.getJoinedGroups().remove(watchPartyToRemove);
                    profileService.updateProfile(memberToRemove.getId(), memberToRemove);
                }
                watchPartyRepository.delete(watchPartyToRemove);
                return true;
            }
            else{
                return false;
            }
        }
    }

    public boolean leaveWatchParty(Integer watchPartyId, Profile removeProfile){
        Optional<WatchParty> storedWatchParty = watchPartyRepository.findById(watchPartyId);
        if(!storedWatchParty.isPresent() || !removeProfile.equals(userService.getCurrentUser().getProfile())){
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

    public void removeMovie(Integer watchPartyId, Integer movieId){
        Optional<WatchParty> storedWatchParty = watchPartyRepository.findById(watchPartyId);
        if(storedWatchParty.isPresent()){
            WatchParty watchParty = storedWatchParty.get();
            watchParty.getMovies().remove(movieId);
            watchPartyRepository.save(watchParty);
        }
    }

    public void removeMember(Integer watchPartyId, Integer memberId){
        Optional<WatchParty> storedWatchParty = watchPartyRepository.findById(watchPartyId);
        if(storedWatchParty.isPresent()){
            WatchParty watchParty = storedWatchParty.get();
            if(watchParty.getLeader().equals(userService.getCurrentUser().getProfile())) {
                Profile storedProfile = profileService.getProfileById(memberId);
                watchParty.getMembers().remove(storedProfile);
                watchPartyRepository.save(watchParty);
            }
        }
    }

    public List<MovieDTO> getTopRatedMovies(WatchParty watchParty){
        Set<MovieDTO> topRatedMovies = new HashSet<>();
        for(Profile member : watchParty.getMembers()){
            topRatedMovies.addAll(profileService.getTopRatedMovies(member));
        }
        return new ArrayList<>(topRatedMovies);
    }

}