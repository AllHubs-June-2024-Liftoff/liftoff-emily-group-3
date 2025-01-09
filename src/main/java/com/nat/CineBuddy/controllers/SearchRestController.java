package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("search")
public class SearchRestController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("profiles")
    public Iterable<Profile> allSearchableProfiles(@RequestParam String search){
        return profileService.searchAllPublicProfiles(search);
    }

}
