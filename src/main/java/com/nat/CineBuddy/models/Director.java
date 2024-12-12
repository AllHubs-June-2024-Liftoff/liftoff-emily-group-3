package com.nat.CineBuddy.models;

import jakarta.persistence.Entity;

@Entity
public class Director extends AbstractMovieEntity {

    public String firstName;
    public String lastName;

    public Director() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

