package com.nat.CineBuddy.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Movie {

    @Id
    @GeneratedValue
    private Integer id;

    private String movieId;
    private String title;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private String genres;
    private String budget;
    private String revenue;
    private String runtime;
    private String voteAverage;

    // Constructors

    //empty constructor for sql database connection
    public Movie() {
    }


    public Movie(String movieId, String title, String overview, String releaseDate, String posterPath) {
        this.movieId = movieId;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    public Movie(String movieId, String title, String overview, String releaseDate, String posterPath, String genres, String budget, String revenue, String runtime, String voteAverage) {
        this.movieId = movieId;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.genres = genres;
        this.budget = budget;
        this.revenue = revenue;
        this.runtime = runtime;
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getFormattedGenres() {
        return String.join(", ", this.genres);
    }
}
