package com.nat.CineBuddy.models;

public class Review {
    private String movieName;
    private String movieId;
    private String reviewerName;
    private String reviewText;
    private int rating;

    //this class doesn't do anything yet
    //empty constructor for sql database connection
    public Review() {
    }

    public Review(String movieName, String movieId, String reviewerName, String reviewText, int rating) {
        this.movieName = movieName;
        this.movieId = movieId;
        this.reviewerName = reviewerName;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
