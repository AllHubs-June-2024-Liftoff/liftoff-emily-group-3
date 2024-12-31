package com.nat.CineBuddy.models;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer movieId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "movieId", insertable = false, updatable = false)
    private Movie movie;  // Optional - if you want to access the full Movie object in your code

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    private int rating;
    private String content;
    private String movieTitle;

    private LocalDateTime dateCreated;

    public Review() {
    }

    public Review(Integer id, int rating, String content) {
        this.id = id;
        this.rating = rating;
        this.content = content;
    }

    public Review(Integer id, int rating, String content, String movieTitle, LocalDateTime dateCreated) {
        this.id = id;
        this.rating = rating;
        this.content = content;
        this.movieTitle = movieTitle;
        this.dateCreated = dateCreated;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser (User user) {
        this.user = user;
    }

}
