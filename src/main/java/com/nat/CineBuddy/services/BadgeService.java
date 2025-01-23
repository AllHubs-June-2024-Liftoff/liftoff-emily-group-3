package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Badge;
import com.nat.CineBuddy.repositories.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BadgeService {
    @Autowired
    private BadgeRepository badgeRepository;

    public void awardBadge(String username, String badgeName) {

        boolean badgeExists = badgeRepository.existsByUsernameAndBadgeName(username, badgeName);

        if (!badgeExists) {
            Badge badge = new Badge();
            badge.setUsername(username);
            badge.setBadgeName(badgeName);
            badgeRepository.save(badge);
        }
    }

    public List<Badge> getUserBadges(String username) {
        return badgeRepository.findByUsername(username);
    }
}
