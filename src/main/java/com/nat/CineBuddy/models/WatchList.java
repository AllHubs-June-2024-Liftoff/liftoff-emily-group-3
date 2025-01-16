package com.nat.CineBuddy.models;

import com.nat.CineBuddy.dto.MovieDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class WatchList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ElementCollection
    @Lob
    private List<String> movies = new ArrayList<>(); // Store JSON strings

    @ManyToOne
    @JoinColumn(name = "profile_id")
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

    public List<String> getMovies() {
        return movies;
    }

    public void setMovies(List<String> movies) {
        this.movies = movies;
    }

    public void addMovie(MovieDTO movie) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(movie);
            this.movies.add(json);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing MovieDTO", e);
        }
    }

    public List<MovieDTO> getMoviesAsDTOs() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<MovieDTO> movieDTOs = new ArrayList<>();
            for (String json : movies) {
                movieDTOs.add(mapper.readValue(json, MovieDTO.class));
            }
            return movieDTOs;
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing JSON to MovieDTO", e);
        }
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
