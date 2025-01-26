package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.repositories.VoteRepository;
import com.nat.CineBuddy.repositories.WatchPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;


    /**
     * Cast a vote for a movie.
     *
     * @param watchParty   The group where the vote is being cast.
     * @param movieId The ID of the movie being voted for.
     * @return True if the vote is successfully cast, false if the user already voted.
     */
    public boolean castVote(WatchParty watchParty, Integer movieId, Profile profile) {
        // Check if the user has already voted in this WatchParty
        List<Vote> existingVotes = voteRepository.findByWatchParty(watchParty);
        for (Vote vote : existingVotes) {
            if (vote.getProfile() != null && vote.getProfile().equals(profile)) {
                return false; // User has already voted
            }
        }

        // Save the new vote
        Vote vote = new Vote();
        vote.setWatchParty(watchParty);
        vote.setProfile(profile);
        vote.setMovieId(movieId);
        voteRepository.save(vote);

        return true;
    }

    /**
     * Get the most voted movie in a group.
     *
     * @param watchParty The group whose votes are being checked.
     * @return The ID of the most voted movie.
     */
    public Integer getMostVotedMovie(WatchParty watchParty) {
        Map<Integer, Integer> voteCounts = getAllVoteCounts(watchParty);

        return voteCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("No votes yet"))
                .getKey();
    }

    /**
     * Get the current vote counts for all movies in a group.
     *
     * @param watchParty The group whose votes are being checked.
     * @return A map of movie IDs to their vote counts.
     */
    public Map<Integer, Integer> getAllVoteCounts(WatchParty watchParty) {
        List<Vote> votes = voteRepository.findByWatchParty(watchParty);
        Map<Integer, Integer> voteCounts = new HashMap<>();

        for (Vote vote : votes) {
            voteCounts.put(vote.getMovieId(), voteCounts.getOrDefault(vote.getMovieId(), 0) + 1);
        }

        return voteCounts;
    }

    /**
     *
     * @param watchParty Passing in watchParty to get all votes.
     * @return All vote objects associated with the watchparty
     */
    public List<Vote> getAllVotes (WatchParty watchParty) {
        return voteRepository.findByWatchParty(watchParty);
    }

    /**
     *
     * @param watchParty object passed in.
     * @return any vote that matches the profile.
     */
    public boolean hasAlreadyVoted (WatchParty watchParty, Profile profile) {
        return getAllVotes(watchParty).stream().anyMatch(vote -> vote.getProfile().equals(profile));
    }


    /**
     *
     * @param watchParty object passed in.  Gets profile
     * @return false if no votes, if votes delete all.
     */
    public boolean retractVote (WatchParty watchParty, Profile profile) {
        List<Vote> userVotes = getAllVotes(watchParty).stream()
                .filter(vote -> vote.getProfile().equals(profile)).toList();

        if(userVotes.isEmpty()){
            return false;
        }

        voteRepository.deleteAll(userVotes);
        return true;

    }

    /**
     *
     * @param watchParty pass in watchParty object.
     * Utilize watchParty object to set our most voted movie.
     * @return most voted movie.
     */
    public Integer finalizeVotes(WatchParty watchParty){
        return getMostVotedMovie(watchParty);
    }

    /**
     *
     * @param watchParty take watchparty as object for spacific watchparty.
     * @return true if votes exist and all votes are deleted in voteRepository.
     */
    public boolean deleteAllVotes(WatchParty watchParty){
        List<Vote> allVotes = voteRepository.findByWatchParty(watchParty);

        if(allVotes.isEmpty()){
            return false; //No votes to delete
        }

        voteRepository.deleteAll(allVotes);
        return true;
    }


}
