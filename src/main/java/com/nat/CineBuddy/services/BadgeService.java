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
            badge.setBadgeDescription("Awarded for submitting your first review!");
            badgeRepository.save(badge);
        }

        if (oneStarReviews >= 3) {
            boolean haterBadgeExists = badgeRepository.existsByProfileIdAndBadgeName(id, "Hater");

            // Prevent duplicate badge awards
            if (!haterBadgeExists) {
                Badge newBadge = new Badge();
                newBadge.setProfile(profile);
                newBadge.setBadgeName("Hater");
                newBadge.setBadgeDescription("Given for submitting 3 or more 1-star reviews.");
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
                badge.setBadgeDescription("Given for reviewing 3 or more horror movies.");
                badgeRepository.save(badge);
            }
        }

        int animationReviewsFourOrFiveStars = reviewRepository.countByProfileIdAndGenreContainingAndRatingGreaterThanEqual(id, "Animation", 4);

        if (animationReviewsFourOrFiveStars >= 10) {
            boolean animationBadgeExists = badgeRepository.existsByProfileIdAndBadgeName(id, "Animation Admirer");
            if (!animationBadgeExists) {
                Badge badge = new Badge();
                badge.setProfile(profile);
                badge.setBadgeName("Animation Admirer");
                badge.setBadgeDescription("Awarded for giving 10+ animated movies a star rating of 4 or higher");
                badgeRepository.save(badge);
            }
        }

        int romanceReviewsFourOrFiveStars = reviewRepository.countByProfileIdAndGenreContainingAndRatingGreaterThanEqual(id, "Romance", 4);

        if (romanceReviewsFourOrFiveStars >= 10) {
            boolean romanceBadgeExists = badgeRepository.existsByProfileIdAndBadgeName(id, "Hopeless Romantic");
                if (!romanceBadgeExists) {
                    Badge badge = new Badge();
                    badge.setProfile(profile);
                    badge.setBadgeName("Hopeless Romantic");
                    badge.setBadgeDescription("Awarded for giving 10+ romance movies a star rating of 4 or higher");
                    badgeRepository.save(badge);
            }
        }



    }



    public List<Badge> getUserBadges(int profileId) {
        return badgeRepository.findByProfileId(profileId);
    }
}