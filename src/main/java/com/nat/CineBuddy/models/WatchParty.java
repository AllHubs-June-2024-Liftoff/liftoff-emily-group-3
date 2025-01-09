package com.nat.CineBuddy.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class WatchParty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    @NotEmpty(message="Watch Party name is required")
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    private Profile leader;

    @ElementCollection
    @CollectionTable(name = "watchparty_votes", joinColumns = @JoinColumn(name = "watchparty_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "movie_id")
    private Map<Integer, Integer> votes = new HashMap<>(); // Tracks votes: userId -> movieId


    private List<Integer> movies;
    private Integer movieChoice;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private List<Profile> members;



    public WatchParty() {}

    public WatchParty(Integer id, String name, Profile leader, List<Integer> movies, Integer movieChoice, List<Profile> members) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.movies = movies;
        this.movieChoice = movieChoice;
        this.members = members;
    }

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



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Profile getLeader() {
        return leader;
    }

    public void setLeader(Profile leader) {
        this.leader = leader;
    }

    public List<Integer> getMovies() {
        return movies;
    }

    public void setMovies(List<Integer> movies) {
        this.movies = movies;
    }

    public Integer getMovieChoice() {
        return movieChoice;
    }

    public void setMovieChoice(Integer movieChoice) {
        this.movieChoice = movieChoice;
    }

    public List<Profile> getMembers() {
        return members;
    }

    public void setMembers(List<Profile> members) {
        this.members = members;
    }

    public Map<Integer, Integer> getVotes() {
        return votes;
    }

    public void setVotes(Map<Integer, Integer> votes) {
        this.votes = votes;
    }



    @Override
    public String toString() {
        return "WatchParty{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", leader=" + (leader != null ? leader.getName() : null) + // Avoid circular reference
            ", movies=" + movies +
            ", movieChoice=" + movieChoice +
            ", members=" + (members != null ? members.size() : 0) + // Avoid circular reference
        '}';
    }
}
