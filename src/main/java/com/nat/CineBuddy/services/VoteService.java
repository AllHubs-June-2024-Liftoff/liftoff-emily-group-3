package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserService userService;


    /**
     * Cast a vote for a movie.
     *
     * @param watchParty   The group where the vote is being cast.
     * @param movieId The ID of the movie being voted for.
     * @return True if the vote is successfully cast, false if the user already voted.
     */
    public boolean castVote(WatchParty watchParty, Integer movieId) {
        // Check if the user has already voted in this WatchParty
        List<Vote> existingVotes = voteRepository.findByWatchParty(watchParty);
        Profile profile = userService.getCurrentUser().getProfile();
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
}
