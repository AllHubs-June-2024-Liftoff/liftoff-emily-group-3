package com.nat.CineBuddy.controllers;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.User;
import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.services.UserService;
import com.nat.CineBuddy.services.VoteService;
import com.nat.CineBuddy.services.WatchPartyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VoteControllerStandaloneTest {

    private MockMvc mockMvc;

    // Pure Mockito mocks (no Spring Boot @MockBean)
    @Mock private VoteService voteService;
    @Mock private WatchPartyService watchPartyService;
    @Mock private UserService userService;

    private VoteController controller;

    @BeforeEach
    void setup() {
        controller = new VoteController();
        // Inject mocks into the @Autowired fields using reflection
        ReflectionTestUtils.setField(controller, "voteService", voteService);
        ReflectionTestUtils.setField(controller, "watchPartyService", watchPartyService);
        ReflectionTestUtils.setField(controller, "userService", userService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // ---------- helpers ----------
    private WatchParty wp() { return new WatchParty(); }
    private Profile profile() { return new Profile(); }
    private User userWith(Profile p) { User u = new User(); u.setProfile(p); return u; }

    // ---------- tests ----------

    @Test
    void getVoteCounts_returnsJsonMap() throws Exception {
        int wpId = 1;
        WatchParty party = wp();
        when(watchPartyService.getWatchParty(wpId)).thenReturn(party);
        when(voteService.getAllVoteCounts(party)).thenReturn(Map.of(10,3, 20,1));

        mockMvc.perform(get("/votes/{watchPartyId}/votes", wpId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.10").value(3))
                .andExpect(jsonPath("$.20").value(1));
    }

    @Test
    void results_noVotes_redirectsWithInfoFlash() throws Exception {
        int wpId = 42;
        WatchParty party = wp();
        when(watchPartyService.getWatchParty(wpId)).thenReturn(party);
        when(voteService.getMostVotedMovieId(party)).thenReturn(Optional.empty());

        mockMvc.perform(get("/votes/{watchPartyId}/results", wpId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/watchparty/" + wpId))
                .andExpect(flash().attribute("info", "No votes yet for this watch party."));
    }

    @Test
    void results_hasWinner_redirectsWithWinnerFlash() throws Exception {
        int wpId = 7;
        int winner = 1234;
        WatchParty party = wp();
        when(watchPartyService.getWatchParty(wpId)).thenReturn(party);
        when(voteService.getMostVotedMovieId(party)).thenReturn(Optional.of(winner));

        mockMvc.perform(get("/votes/{watchPartyId}/results", wpId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/watchparty/" + wpId))
                .andExpect(flash().attribute("winnerMovieId", winner));
    }

    @Test
    void castVote_success_setsSuccessFlash_andRedirects() throws Exception {
        int wpId = 11; int movieId = 9001;
        WatchParty party = wp(); Profile p = profile();
        when(watchPartyService.getWatchParty(wpId)).thenReturn(party);
        when(userService.getCurrentUser()).thenReturn(userWith(p));
        when(voteService.castVote(party, movieId, p)).thenReturn(true);

        mockMvc.perform(post("/votes/{watchPartyId}/vote", wpId)
                        .param("movieId", String.valueOf(movieId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/watchparty/" + wpId))
                .andExpect(flash().attribute("success", "Vote submitted."));
    }

    @Test
    void castVote_alreadyVoted_setsInfoFlash_andRedirects() throws Exception {
        int wpId = 12; int movieId = 100;
        WatchParty party = wp(); Profile p = profile();
        when(watchPartyService.getWatchParty(wpId)).thenReturn(party);
        when(userService.getCurrentUser()).thenReturn(userWith(p));
        when(voteService.castVote(party, movieId, p)).thenReturn(false);

        mockMvc.perform(post("/votes/{watchPartyId}/vote", wpId)
                        .param("movieId", String.valueOf(movieId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/watchparty/" + wpId))
                .andExpect(flash().attribute("info", "You have already voted."));
    }

    @Test
    void getAllVotes_post_returnsJsonArray() throws Exception {
        int wpId = 5;
        WatchParty party = wp();
        Vote v1 = new Vote(); v1.setMovieId(1);
        Vote v2 = new Vote(); v2.setMovieId(2);

        when(watchPartyService.getWatchParty(wpId)).thenReturn(party);
        when(voteService.getAllVotes(party)).thenReturn(List.of(v1, v2));

        mockMvc.perform(post("/votes/{watchPartyId}/all", wpId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].movieId").value(1))
                .andExpect(jsonPath("$[1].movieId").value(2));
    }

    @Test
    void retractVote_redirectsBack() throws Exception {
        int wpId = 77;
        WatchParty party = wp(); Profile p = profile();
        when(watchPartyService.getWatchParty(wpId)).thenReturn(party);
        when(userService.getCurrentUser()).thenReturn(userWith(p));
        when(voteService.retractVote(party, p)).thenReturn(true);

        mockMvc.perform(post("/votes/{watchPartyId}/retract", wpId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/watchparty/" + wpId));
    }

    @Test
    void deleteAllVotes_unauthorized_returnsPlainMessage() throws Exception {
        int wpId = 30;
        WatchParty party = wp(); Profile leader = profile(); Profile current = profile();
        party.setLeader(leader);
        when(watchPartyService.getWatchParty(wpId)).thenReturn(party);
        when(userService.getCurrentUser()).thenReturn(userWith(current));

        mockMvc.perform(post("/votes/{watchPartyId}/deleteAll", wpId))
                .andExpect(status().isOk())
                .andExpect(content().string("You are not authorized to delete votes for this watchparty."));
    }

    @Test
    void deleteAllVotes_authorized_successMessage() throws Exception {
        int wpId = 31;
        WatchParty party = wp(); Profile same = profile();
        party.setLeader(same);
        when(watchPartyService.getWatchParty(wpId)).thenReturn(party);
        when(userService.getCurrentUser()).thenReturn(userWith(same));
        when(voteService.deleteAllVotes(party)).thenReturn(true);

        mockMvc.perform(post("/votes/{watchPartyId}/deleteAll", wpId))
                .andExpect(status().isOk())
                .andExpect(content().string("All votes for this watch party have been deleted successfully."));
    }

    @Test
    void deleteAllVotes_authorized_noVotesMessage() throws Exception {
        int wpId = 32;
        WatchParty party = wp(); Profile same = profile();
        party.setLeader(same);
        when(watchPartyService.getWatchParty(wpId)).thenReturn(party);
        when(userService.getCurrentUser()).thenReturn(userWith(same));
        when(voteService.deleteAllVotes(party)).thenReturn(false);

        mockMvc.perform(post("/votes/{watchPartyId}/deleteAll", wpId))
                .andExpect(status().isOk())
                .andExpect(content().string("No votes found to delete."));
    }
}
