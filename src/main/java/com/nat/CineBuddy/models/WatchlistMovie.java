package com.nat.CineBuddy.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import org.springframework.data.annotation.Id;


@Entity
public class WatchlistMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer movieId;  // ID from TMDB API

    private String title;     // Movie title (from API)
    private String releaseDate; // Release date (from API)
    private String posterPath;  // Poster path (from API)

    private boolean watched;  // Application-specific field
    private String comment;   // Application-specific field

    @ManyToOne
    private Watchlist watchlist;

    // Constructors
    public WatchlistMovie() {}

    public WatchlistMovie(Integer movieId, String title, String releaseDate, String posterPath) {
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    public WatchlistMovie(Movie movie, boolean watched, String comment) {
        this.movieId = movie.getId() != null ? Integer.parseInt(movie.getId().toString()) : null;
        this.title = movie.getTitle();
        this.releaseDate = movie.getReleaseDate();
        this.posterPath = movie.getPosterPath();
        this.watched = watched;
        this.comment = comment;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Watchlist getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(Watchlist watchlist) {
        this.watchlist = watchlist;
    }
}
