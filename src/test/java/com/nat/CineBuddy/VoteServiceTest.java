package com.nat.CineBuddy;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.repositories.VoteRepository;
import com.nat.CineBuddy.services.UserService;
import com.nat.CineBuddy.services.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private VoteService voteService;

    private Profile testProfile;
    private WatchParty testWatchParty;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock a User and Profile
        User testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");

        testProfile = new Profile();
        testProfile.setId(1);
        testProfile.setName("Test Profile");
        testProfile.setUser(testUser);

        testUser.setProfile(testProfile); // Ensure the User and Profile are linked

        // Mock a WatchParty
        testWatchParty = new WatchParty();
        testWatchParty.setId(1);
        testWatchParty.setName("Test Watch Party");

        // Mock userService to return the mocked User
        when(userService.getCurrentUser()).thenReturn(testProfile.getUser());
    }

    @Test
    void testCastVote_Success() {
        // Mock no existing votes
        when(voteRepository.findByWatchParty(testWatchParty)).thenReturn(new ArrayList<>());

        // Call the castVote method
        boolean result = voteService.castVote(testWatchParty, 101);

        // Verify behavior
        assertTrue(result, "Vote should be cast successfully");
        verify(voteRepository, times(1)).save(any(Vote.class));
    }

    @Test
    void testCastVote_Failure_AlreadyVoted() {
        // Mock an existing vote
        Vote existingVote = new Vote();
        existingVote.setProfile(testProfile);
        existingVote.setMovieId(101);

        List<Vote> existingVotes = new ArrayList<>();
        existingVotes.add(existingVote);

        when(voteRepository.findByWatchParty(testWatchParty)).thenReturn(existingVotes);

        // Call the castVote method
        boolean result = voteService.castVote(testWatchParty, 101);

        // Verify behavior
        assertFalse(result, "Vote should not be cast if user already voted");
        verify(voteRepository, times(0)).save(any(Vote.class));
    }

    @Test
    void testVoteSavedCorrectly() {
        // Mock no existing votes
        when(voteRepository.findByWatchParty(testWatchParty)).thenReturn(new ArrayList<>());

        // Call the castVote method
        voteService.castVote(testWatchParty, 101);

        // Verify the saved vote has the correct properties
        verify(voteRepository).save(argThat(vote -> {
            System.out.println("Expected Profile: " + testProfile);
            System.out.println("Actual Profile: " + vote.getProfile());
            System.out.println("Expected WatchParty: " + testWatchParty);
            System.out.println("Actual WatchParty: " + vote.getWatchParty());
            return vote.getProfile() != null &&
                    vote.getProfile().equals(testProfile) &&
                    vote.getMovieId().equals(101) &&
                    vote.getWatchParty().equals(testWatchParty);
        }));
    }
}
