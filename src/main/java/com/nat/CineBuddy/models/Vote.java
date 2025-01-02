package com.nat.CineBuddy.models;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Unique identifier for the vote session

    @ManyToOne
    private Group group; // Links the votes to a group

    @ElementCollection
    @CollectionTable(name = "user_votes", joinColumns = @JoinColumn(name = "vote_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "movie_id")
    private Map<Integer, Integer> votes = new HashMap<>(); // Tracks user votes (userId -> movieId)

    // Add a vote
    public boolean addVote(Integer userId, Integer movieId) {
        if (votes.containsKey(userId)) {
            return false; // User has already voted
        }
        votes.put(userId, movieId); // Add the user's vote
        return true;
    }

    // Get vote counts for all movies
    public Map<Integer, Integer> getVoteCounts() {
        Map<Integer, Integer> movieCounts = new HashMap<>();
        for (Integer movieId : votes.values()) {
            movieCounts.put(movieId, movieCounts.getOrDefault(movieId, 0) + 1);
        }
        return movieCounts;
    }

    // Get the most voted movie
    public Integer getMostVotedMovie() {
        return getVoteCounts().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("No votes yet"))
                .getKey();
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Map<Integer, Integer> getVotes() {
        return votes;
    }

    public void setVotes(Map<Integer, Integer> votes) {
        this.votes = votes;
    }
}
