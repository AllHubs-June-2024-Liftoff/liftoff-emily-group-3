package com.nat.CineBuddy.services;

import com.nat.CineBuddy.repositories.WatchPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WatchPartyServiceImpl implements WatchPartyService{

    @Autowired
    private WatchPartyRepository watchPartyRepository;

}
