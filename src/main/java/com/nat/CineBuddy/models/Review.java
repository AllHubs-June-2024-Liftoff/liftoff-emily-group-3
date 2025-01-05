package com.nat.CineBuddy.models;

import com.nat.CineBuddy.dto.MovieDTO;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Transient
    private MovieDTO movieDTO;  // Not persisted in the database, used for transfer of movie data

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;  // Maps to the User entity

    private int rating;
    private String content;
    private LocalDateTime dateCreated;

    public Review() {
    }

    public Review(Integer id, int rating, String content, LocalDateTime dateCreated, MovieDTO movieDTO) {
        this.id = id;
        this.rating = rating;
        this.content = content;
        this.dateCreated = dateCreated;
        this.movieDTO = movieDTO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MovieDTO getMovieDTO() {
        return movieDTO;
    }

    public void setMovieDTO(MovieDTO movieDTO) {
        this.movieDTO = movieDTO;
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

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    // Helper method to fetch the id from MovieDTO
    public String getMovieId() {
        return movieDTO != null ? movieDTO.getId() : null;
    }

    public void setMovieId(Integer id) {
    }
}
