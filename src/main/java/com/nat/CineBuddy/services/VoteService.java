package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Group;
import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    /**
     * Cast a vote for a movie.
     *
     * @param group   The group where the vote is being cast.
     * @param userId  The ID of the user casting the vote.
     * @param movieId The ID of the movie being voted for.
     * @return True if the vote is successfully cast, false if the user already voted.
     */
    public boolean castVote(Group group, Integer userId, Integer movieId) {
        // Retrieve the Vote object for the group or create a new one
        Vote vote = group.getVotes().stream().findFirst().orElse(new Vote());

        // Add the vote
        boolean success = vote.addVote(userId, movieId);

        // Save the updated vote
        if (success) {
            vote.setGroup(group);
            voteRepository.save(vote);
        }

        return success;
    }

    /**
     * Get the most voted movie in a group.
     *
     * @param group The group whose votes are being checked.
     * @return The ID of the most voted movie.
     */
    public Integer getMostVotedMovie(Group group) {
        Vote vote = group.getVotes().stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No votes yet"));
        return vote.getMostVotedMovie();
    }

    /**
     * Get the current vote counts for all movies in a group.
     *
     * @param group The group whose votes are being checked.
     * @return A map of movie IDs to their vote counts.
     */
    public Map<Integer, Integer> getAllVoteCounts(Group group) {
        // Retrieve the Vote object for the group
        Vote vote = group.getVotes().stream().findFirst().orElse(new Vote());

        // Generate a map of movie IDs and their vote counts
        return vote.getVoteCounts();
    }
}
