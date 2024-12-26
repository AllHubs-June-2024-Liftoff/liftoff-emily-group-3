package com.nat.CineBuddy.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    @NotEmpty(message="Group name is required")
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Profile leader;
    private List<Movie> movies;
    private List<Profile> members;

    public Group() {}

    public Group(Integer id, String name, Profile leader, List<Movie> movies, List<Profile> members) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.movies = movies;
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

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Profile> getMembers() {
        return members;
    }

    public void setMembers(List<Profile> members) {
        this.members = members;
    }
}
