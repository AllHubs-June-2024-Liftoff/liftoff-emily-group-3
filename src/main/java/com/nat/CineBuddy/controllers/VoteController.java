package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Group;
import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.services.GroupService;
import com.nat.CineBuddy.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private GroupService groupService;

    /**
     * Cast a vote for a movie in a group.
     */
    @PostMapping("/{groupId}/vote")
    public String castVote(@PathVariable Integer groupId, @RequestParam Integer userId, @RequestParam Integer movieId) {
        Group group = groupService.findById(groupId); // Find the group
        boolean success = voteService.castVote(group, userId, movieId); // Cast vote

        return success ? "Vote cast successfully!" : "You have already voted for this movie.";
    }

    /**
     * Retrieve the current vote counts for all movies in a group.
     */
    @GetMapping("/{groupId}/votes")
    public Map<Integer, Integer> getVoteCounts(@PathVariable Integer groupId) {
        Group group = groupService.findById(groupId); // Find the group
        return voteService.getAllVoteCounts(group); // Get vote counts
    }

    /**
     * Get the most voted movie in a group.
     */
    @GetMapping("/{groupId}/results")
    public String getMostVotedMovie(@PathVariable Integer groupId) {
        Group group = groupService.findById(groupId); // Find the group
        return "The most voted movie is: " + voteService.getMostVotedMovie(group);
    }
}
