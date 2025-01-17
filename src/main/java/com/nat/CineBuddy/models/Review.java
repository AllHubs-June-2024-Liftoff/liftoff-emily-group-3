package com.nat.CineBuddy.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String movieId;
    private String username;
    private int rating;
    private String content;
    private String movieTitle;

    private LocalDateTime dateCreated;

    public Review() {
    }

    public Review(Integer id, String movieId, String username, int rating, String content) {
        this.id = id;
        this.movieId = movieId;
        this.username = username;
        this.rating = rating;
        this.content = content;
    }

    public Review(Integer id, String movieId, String username, int rating, String content, String movieTitle, LocalDateTime dateCreated) {
        this.id = id;
        this.movieId = movieId;
        this.username = username;
        this.rating = rating;
        this.content = content;
        this.movieTitle = movieTitle;
        this.dateCreated = dateCreated;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}