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
    private WatchParty watchParty;// Links the votes to a watchParty

    private Integer userId; // The ID of the user who voted

    private Integer movieId; // The ID of the movie the user voted for



    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public WatchParty getWatchParty() {
        return watchParty;
    }

    public void setWatchParty(WatchParty watchParty) {
        this.watchParty = watchParty;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }
}
