package com.nat.CineBuddy.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nat.CineBuddy.dto.MovieDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class WatchList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String name;

    @ElementCollection
    @Lob
    private List<String> movieIds = new ArrayList<>(); // Storing movie IDs instead of full JSON

    @ManyToOne
    @JoinColumn(name = "profile_id")
    @NotNull
    private Profile profile;

    public WatchList() {}

    public WatchList(String name, Profile profile) {
        this.name = name;
        this.profile = profile;
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

    public List<String> getMovieIds() {
        return movieIds;
    }

    public void setMovieIds(List<String> movieIds) {
        this.movieIds = movieIds;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void addMovie(String movieId) {
        this.movieIds.add(movieId);
    }

    public void removeMovie(String movieId) {
        this.movieIds.remove(movieId);
    }
}
