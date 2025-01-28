package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Badge;
import com.nat.CineBuddy.models.Profile;
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

    @Autowired
    private ProfileService profileService;

    public void awardBadge(Integer id) {
        Profile profile = profileService.getProfileById(id);

        if (profile == null) {
            throw new IllegalArgumentException("Profile not found for profile: " + id);
        }

        boolean badgeExists = badgeRepository.existsByProfileIdAndBadgeName(id, "First Review");

        int oneStarReviews = reviewRepository.countByProfileIdAndRating(id, 1);

        if (!badgeExists) {
            Badge badge = new Badge();
            badge.setProfile(profile);
            badge.setBadgeName("First Review");
            badgeRepository.save(badge);
        }

        if (oneStarReviews >= 3) {
            boolean haterBadgeExists = badgeRepository.existsByProfileIdAndBadgeName(id, "Hater");

            // Prevent duplicate badge awards
            if (!haterBadgeExists) {
                Badge newBadge = new Badge();
                newBadge.setProfile(profile);
                newBadge.setBadgeName("Hater");
                badgeRepository.save(newBadge);
            }
        }

        int horrorReviews = reviewRepository.countByProfileIdAndGenreContaining(id, "Horror");
        if (horrorReviews >= 3) {
            boolean horrorBadgeExists = badgeRepository.existsByProfileIdAndBadgeName(id, "Horror Enthusiast");
            if (!horrorBadgeExists) {
                Badge badge = new Badge();
                badge.setProfile(profile);
                badge.setBadgeName("Horror Enthusiast");
                badgeRepository.save(badge);
            }
        }
    }



    public List<Badge> getUserBadges(int profileId) {
        return badgeRepository.findByProfileId(profileId);
    }
}