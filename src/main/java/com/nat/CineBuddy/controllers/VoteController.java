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
     * @param groupId The ID of the group where the vote is being cast.
     * @param userId The ID of the user casting the vote.
     * @param movieId The ID of the movie being voted for.
     * @return A success or failure message.
     */
    @PostMapping("/{groupId}/vote")
    public String castVote(@PathVariable Integer groupId, @RequestParam Integer userId, @RequestParam Integer movieId) {
        // Retrieve the group
        Group group = groupService.findById(groupId);

        // Cast the vote via the service
        boolean success = voteService.castVote(group, userId, movieId);

        if (success) {
            return "Vote cast successfully!";
        } else {
            return "You have already voted for this movie.";
        }
    }



}
