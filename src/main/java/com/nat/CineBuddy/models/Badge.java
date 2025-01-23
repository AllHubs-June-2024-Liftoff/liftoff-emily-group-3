package com.nat.CineBuddy.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Badge {

    @Id
    @GeneratedValue
    private Integer id;

    private String username;

    private String badgeName;

    public Badge() {
    }

    public Badge(Integer id, String username, String badgeName) {
        this.id = id;
        this.username = username;
        this.badgeName = badgeName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }
}


