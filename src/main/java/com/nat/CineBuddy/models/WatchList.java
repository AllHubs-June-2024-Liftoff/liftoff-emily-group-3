package com.nat.CineBuddy.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nat.CineBuddy.dto.MovieDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class WatchList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotEmpty(message="Please enter your Watch List name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Profile profile;
    private static List<Integer> movies;

    public WatchList() {}

    public WatchList(Integer id, String name, Profile profile, List<Integer> movies) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.movies = movies;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotEmpty(message = "Please enter your Watch List name") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "Please enter your Watch List name") String name) {
        this.name = name;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public static List<Integer> getMovies() {
        return movies;
    }

    public static void setMovies(List<Integer> movies) {
        WatchList.movies = movies;
    }

    @Override
    public String toString() {
        return "WatchList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", movies=" + movies +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WatchList that = (WatchList) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

