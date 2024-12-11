package com.nat.CineBuddy.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
    public class WatchList {
        @Id
        @GeneratedValue
        private int id;
        private String title; // title of the moving being added to WatchList


    }

