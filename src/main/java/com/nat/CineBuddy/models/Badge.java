package com.nat.CineBuddy.models;

import jakarta.persistence.*;

@Entity
public class Badge {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Profile profile;

    private String badgeName;

    private String badgeDescription;

    public Badge() {
    }

    public Badge(Integer id, Profile profile, String badgeName) {
        this.id = id;
        this.profile = profile;
        this.badgeName = badgeName;
    }

    public Badge(String badgeName, String badgeDescription) {
        this.badgeName = badgeName;
        this.badgeDescription = badgeDescription;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public String getBadgeDescription() {
        return badgeDescription;
    }

    public void setBadgeDescription(String badgeDescription) {
        this.badgeDescription = badgeDescription;
    }
}