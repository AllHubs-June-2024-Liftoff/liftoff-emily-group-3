package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.services.WatchPartyService;
import com.nat.CineBuddy.services.VoteService;
import com.nat.CineBuddy.services.WatchPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public String castVote(@PathVariable Integer watchPartyId, @RequestParam Integer movieId) {
        WatchParty watchParty = watchPartyService.viewWatchParty(watchPartyId); // Using existing method instead of findById.
        boolean success = voteService.castVote(watchParty, movieId); // Cast vote

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

    /**
     *
     * @param watchPartyId Getting current watchparty.
     * @return Returning all votes for that watchparty.
     */

    @PostMapping("/{watchPartyId}/all")
    public List<Vote> getAllVotes(@PathVariable Integer watchPartyId) {
        WatchParty watchParty = watchPartyService.viewWatchParty(watchPartyId);
        return voteService.getAllVotes(watchParty);
    }

    /**
     *
     * @param watchPartyId to get spacific watchparty
     * @return String that displays if vote was deleted successfully or if profile has not voted.
     * Returning string will change to boolean if needed.
     */

    @PostMapping("/{watchPartyId}/retract")
    public String retractVote(@PathVariable Integer watchPartyId) {
        WatchParty watchparty = watchPartyService.viewWatchParty(watchPartyId);
        boolean success = voteService.retractVote(watchparty);

        return success ? "Vote retracted successfully!" : "You have not voted yet.";


    }

    /**
     *
     * @param watchPartyId get watchpartyId
     * call watchPartyService to get current watchparty by Id.
     * call voteService.finalizeVotes and pass in watchparty object.
     * @return String and the selected movie by calling .getMovieChoice().
     */
    @PostMapping("/{watchPartyId}/finalize")
    public String finalizeVotes (@PathVariable Integer watchPartyId) {
        WatchParty watchParty = watchPartyService.viewWatchParty(watchPartyId);
        voteService.finalizeVotes(watchParty);
        return "Votes finalized. The selected movie is: " + watchParty.getMovieChoice();
    }




}
