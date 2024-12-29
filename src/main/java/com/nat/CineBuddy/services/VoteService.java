package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.models.Group;
import com.nat.CineBuddy.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    // Add a vote
    public boolean castVote(Group group, Integer userId, Integer movieId) {
        Vote vote = group.getVotes().stream().findFirst().orElse(new Vote());
        vote.addVote(userId, movieId);
        vote.setGroup(group);
        voteRepository.save(vote);
        return false;
    }

    // Get the most voted movie
    public Integer getMostVotedMovie(Group group) {
        Vote vote = group.getVotes().stream().findFirst().orElseThrow(() -> new IllegalArgumentException("No votes yet"));
        return vote.getMostVotedMovie();
    }

    // Retrieve all votes
    public Vote getVotes(Group group) {
        return group.getVotes().stream().findFirst().orElse(null);
    }
}
