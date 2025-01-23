package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Badge;
import com.nat.CineBuddy.repositories.BadgeRepository;
import com.nat.CineBuddy.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BadgeService {
    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public void awardBadge(String username) {

        boolean badgeExists = badgeRepository.existsByUsernameAndBadgeName(username, "First Review");
        int oneStarReviews = reviewRepository.countByUsernameAndRating(username, 1);

        if (!badgeExists) {
            Badge badge = new Badge();
            badge.setUsername(username);
            badge.setBadgeName("First Review");
            badgeRepository.save(badge);
        }

        if (oneStarReviews >= 3) {
            boolean haterBadgeExists = badgeRepository.existsByUsernameAndBadgeName(username, "Hater");

            // Prevent duplicate badge awards
            if (oneStarReviews >= 3 && !haterBadgeExists) {
                Badge newBadge = new Badge();
                newBadge.setUsername(username);
                newBadge.setBadgeName("Hater");
                badgeRepository.save(newBadge);
            }
        }
    }

    public List<Badge> getUserBadges(String username) {
        return badgeRepository.findByUsername(username);
    }
}
