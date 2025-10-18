package com.nat.CineBuddy.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "watch_party_id", nullable = false)
    private WatchParty watchParty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(name = "movie_id", nullable = false)
    private Integer movieId;

    // MySQL 8 maps Instant -> TIMESTAMP; Hibernate will add this column
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
    }


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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", watchParty=" + watchParty +
                ", profile=" + profile +
                ", movieId=" + movieId +
                '}';
    }
}
