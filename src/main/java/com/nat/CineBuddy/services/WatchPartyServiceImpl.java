package com.nat.CineBuddy.services;

import com.nat.CineBuddy.dto.MovieDTO;
import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.repositories.ReviewRepository;
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
    private VoteService voteService;
    @Autowired
    private ReviewRepository reviewRepository;


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
            if(watchParty.getMovies() == null){
                watchParty.setMovies(new ArrayList<Integer>());
            }
            updatedWatchParty.setMovies(watchParty.getMovies());
            updatedWatchParty.setMovieChoice(watchParty.getMovieChoice());
            updatedWatchParty.setMembers(watchParty.getMembers());
            watchPartyRepository.save(updatedWatchParty);
            return true;
        }
    }

    public boolean deleteWatchParty(Integer watchPartyId, Profile leader){
        Optional<WatchParty> storedWatchParty = watchPartyRepository.findById(watchPartyId);
        if(!storedWatchParty.isPresent()){
            return false;
        }
        else{
            WatchParty watchPartyToRemove = storedWatchParty.get();
            if(watchPartyToRemove.getLeader().equals(leader)){
                for (Profile memberToRemove : watchPartyToRemove.getMembers()){
                    memberToRemove.getJoinedGroups().remove(watchPartyToRemove);
                    profileService.updateProfile(memberToRemove.getId(), memberToRemove);
                }
                voteService.deleteAllVotes(watchPartyToRemove);
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
        if(!storedWatchParty.isPresent()){
            return false;
        }
        else{
            WatchParty watchPartyToRemove = storedWatchParty.get();
            watchPartyToRemove.getMembers().remove(removeProfile);
            removeProfile.getJoinedGroups().remove(watchPartyToRemove);
            voteService.retractVote(watchPartyToRemove, removeProfile);
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
            List<Vote> votes = voteService.getAllVotes(watchParty);
            for (Vote vote : votes){
                if(vote.getMovieId().equals(movieId)){
                    voteService.retractVote(watchParty, vote.getProfile());
                }
            }
            watchPartyRepository.save(watchParty);
        }
    }

    public void removeMember(Integer watchPartyId, Integer memberId, Profile leader){
        Optional<WatchParty> storedWatchParty = watchPartyRepository.findById(watchPartyId);
        if(storedWatchParty.isPresent()){
            WatchParty watchParty = storedWatchParty.get();
            if(watchParty.getLeader().equals(leader)) {
                Profile storedProfile = profileService.getProfileById(memberId);
                watchParty.getMembers().remove(storedProfile);
                watchPartyRepository.save(watchParty);
            }
        }
    }

    public List<MovieDTO> getTopRatedMovies(WatchParty watchParty){
        Set<MovieDTO> topRatedMovies = new HashSet<>();
        for(Profile member : watchParty.getMembers()){
            topRatedMovies.addAll(profileService.getTopRatedMovies(reviewRepository.findByProfileIdOrderByRatingDesc(member.getUser().getProfile().getId())));
        }
        return new ArrayList<>(topRatedMovies);
    }

}