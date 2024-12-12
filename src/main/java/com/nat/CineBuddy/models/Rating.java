package com.nat.CineBuddy.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Rating extends AbstractMovieEntity {

    @NotBlank(message = "Rating cannot be empty")
    @Size(max = 100, message = "Rating must be between 0 - 100")
    public double rating;

    public Rating() {
    }

    public double getRating() {
        return rating;
    }

    public void setRating (double rating) {
        this.rating = rating;
    }
}
