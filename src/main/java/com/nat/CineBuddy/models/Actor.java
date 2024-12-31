package com.nat.CineBuddy.models;

public class Actor {
    private String id;
    private String name;
    private String character;
    private String profilePath;

    // Empty constructor for SQL database connection
    public Actor() {
    }

    // Constructor with all fields
    public Actor(String id, String name, String character, String profilePath) {
        this.id = id;
        this.name = name;
        this.character = character;
        this.profilePath = profilePath;
    }

    // Constructor with only name and character (to match the constructor in TMDbService)
    public Actor(String name, String character) {
        this.name = name;
        this.character = character;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
