package com.nat.CineBuddy.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Foreign key column to link the review to the user
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id") // Foreign key column to link the review to the movie
    private Movie movie;

    private int rating;
    private String content;
    private LocalDateTime dateCreated;

    public Review() {
    }

    public Review(Long id, User user, Movie movie, int rating, String content) {
        this.id = id;
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.content = content;
    }

    public Review(Long id, User user, Movie movie, int rating, String content, LocalDateTime dateCreated) {
        this.id = id;
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.content = content;
        this.dateCreated = dateCreated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
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

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
}
