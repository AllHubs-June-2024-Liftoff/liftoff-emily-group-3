package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.services.WatchPartyService;
import com.nat.CineBuddy.services.VoteService;
import com.nat.CineBuddy.services.WatchPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private WatchPartyService watchPartyService;

    /**
     * Cast a vote for a movie in a group.
     */
    @PostMapping("/{watchPartyId}/vote")
    public String castVote(@PathVariable Integer watchPartyId, @RequestParam Integer userId, @RequestParam Integer movieId) {
        WatchParty watchParty = watchPartyService.viewWatchParty(watchPartyId); // Using existing method instead of findById.
        boolean success = voteService.castVote(watchParty, userId, movieId); // Cast vote

        return success ? "Vote cast successfully!" : "You have already voted for this movie.";
    }

    /**
     * Retrieve the current vote counts for all movies in a group.
     */
    @GetMapping("/{watchPartyId}/votes")
    public Map<Integer, Integer> getVoteCounts(@PathVariable Integer watchPartyId) {
        // Use viewWatchParty to fetch the WatchParty entity
        WatchParty watchParty = watchPartyService.viewWatchParty(watchPartyId);
        return voteService.getAllVoteCounts(watchParty);
    }
    /**
     * Get the most voted movie in a group.
     */
    @GetMapping("/{watchPartyId}/results")
    public String getMostVotedMovie(@PathVariable Integer watchPartyId) {
        // Use viewWatchParty to fetch the WatchParty entity
        WatchParty watchParty = watchPartyService.viewWatchParty(watchPartyId);
        return "The most voted movie is: " + voteService.getMostVotedMovie(watchParty);
    }
}
