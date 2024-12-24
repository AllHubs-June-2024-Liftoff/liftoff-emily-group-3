package com.nat.CineBuddy.models;

import com.nat.CineBuddy.models.Movie;
import org.springframework.security.core.userdetails.User;

import java.util.HashMap;
import java.util.Map;

public class Vote {
    private Integer id; // Temporary unique identifier for the vote session
    private Map<User, Movie> votes; // Tracks users and their movie choices

    // Constructor
    public Vote() {
        this.votes = new HashMap<>();
    }

    // Add a vote
    public void addVote(User user, Movie movie) {
        if (votes.containsKey(user)) {
            System.out.println("User has already voted!");
        } else {
            votes.put(user, movie);
        }
    }

    // Retrieve all votes
    public Map<User, Movie> getVotes() {
        return votes;
    }

    // Get the most voted movie
    public Movie getMostVotedMovie() {
        Map<Movie, Integer> movieCount = new HashMap<>();
        for (Movie movie : votes.values()) {
            movieCount.put(movie, movieCount.getOrDefault(movie, 0) + 1);
        }

        return movieCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("No votes yet"))
                .getKey();
    }
}