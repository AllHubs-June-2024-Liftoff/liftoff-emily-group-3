package com.nat.CineBuddy.models;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String movieId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    private int rating;
    private String content;
    private String movieTitle;

    private LocalDateTime dateCreated;

    private String genre;

    public Review() {
    }

    public Review(Integer id, String movieId, Profile profile, int rating, String content) {
        this.id = id;
        this.movieId = movieId;
        this.profile = profile;
        this.rating = rating;
        this.content = content;
    }

    public Review(Integer id, String movieId, Profile profile, int rating, String content, String movieTitle, LocalDateTime dateCreated, String genre) {
        this.id = id;
        this.movieId = movieId;
        this.profile = profile;
        this.rating = rating;
        this.content = content;
        this.movieTitle = movieTitle;
        this.dateCreated = dateCreated;
        this.genre = genre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getFormattedDateCreated() {
        if (dateCreated != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            return dateCreated.format(formatter);
        }
        return "";
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}