package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.services.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TmdbController {

    @Autowired
    private TmdbService tmdbService;

    @GetMapping("/test-api")
    public String testApi() {
        return tmdbService.testConnection();  // Test connection and return the result
    }
}
