package com.nat.CineBuddy.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Entity
public class WatchParty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    @NotEmpty(message="Watch Party name is required")
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Profile leader;
    private List<Integer> movies;
    private Integer movieChoice;
    @ManyToMany(mappedBy = "joinedGroups", cascade = CascadeType.PERSIST)
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
