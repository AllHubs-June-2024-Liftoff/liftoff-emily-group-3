package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.repositories.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private VoteService voteService;

    private WatchParty wp;
    private Profile p1;
    private Profile p2;

    @BeforeEach
    void setup() {
        wp = new WatchParty();
        p1 = new Profile();
        p2 = new Profile();
    }

    @Test
    void castVote_savesWhenUserHasNotVoted() {
        when(voteRepository.findByWatchParty(wp)).thenReturn(List.of());

        boolean ok = voteService.castVote(wp, 101, p1);

        assertTrue(ok);
        ArgumentCaptor<Vote> cap = ArgumentCaptor.forClass(Vote.class);
        verify(voteRepository).save(cap.capture());
        Vote saved = cap.getValue();
        assertEquals(wp, saved.getWatchParty());
        assertEquals(p1, saved.getProfile());
        assertEquals(101, saved.getMovieId());
    }

    @Test
    void castVote_returnsFalseWhenUserAlreadyVoted() {
        Vote existing = new Vote();
        existing.setWatchParty(wp);
        existing.setProfile(p1);

        when(voteRepository.findByWatchParty(wp)).thenReturn(List.of(existing));

        boolean ok = voteService.castVote(wp, 101, p1);

        assertFalse(ok);
        verify(voteRepository, never()).save(any());
    }

    @Test
    void getMostVotedMovieId_empty_returnsEmpty() {
        when(voteRepository.findByWatchParty(wp)).thenReturn(List.of());
        assertTrue(voteService.getMostVotedMovieId(wp).isEmpty());
    }

    @Test
    void getMostVotedMovie_throwsWhenNoVotes() {
        when(voteRepository.findByWatchParty(wp)).thenReturn(List.of());
        assertThrows(IllegalStateException.class, () -> voteService.getMostVotedMovie(wp));
    }

    @Test
    void getMostVotedMovie_returnsWinnerId() {
        Vote v1 = new Vote(); v1.setWatchParty(wp); v1.setMovieId(10); v1.setProfile(p1);
        Vote v2 = new Vote(); v2.setWatchParty(wp); v2.setMovieId(10); v2.setProfile(p2);
        Vote v3 = new Vote(); v3.setWatchParty(wp); v3.setMovieId(20); v3.setProfile(new Profile());
        when(voteRepository.findByWatchParty(wp)).thenReturn(List.of(v1, v2, v3));

        Integer winner = voteService.getMostVotedMovie(wp);
        assertEquals(10, winner);
    }

    @Test
    void getAllVoteCounts_talliesPerMovie() {
        Vote a = new Vote(); a.setWatchParty(wp); a.setMovieId(10); a.setProfile(p1);
        Vote b = new Vote(); b.setWatchParty(wp); b.setMovieId(10); b.setProfile(p2);
        Vote c = new Vote(); c.setWatchParty(wp); c.setMovieId(20); c.setProfile(new Profile());
        when(voteRepository.findByWatchParty(wp)).thenReturn(List.of(a, b, c));

        Map<Integer,Integer> counts = voteService.getAllVoteCounts(wp);

        assertEquals(2, counts.get(10));
        assertEquals(1, counts.get(20));
        assertEquals(2, counts.size());
    }

    @Test
    void getAllVotes_delegatesToRepository() {
        List<Vote> list = List.of(new Vote());
        when(voteRepository.findByWatchParty(wp)).thenReturn(list);

        List<Vote> result = voteService.getAllVotes(wp);
        assertSame(list, result);
    }

    @Test
    void hasAlreadyVoted_trueWhenProfileFound() {
        Vote v = new Vote(); v.setWatchParty(wp); v.setProfile(p1); v.setMovieId(10);
        when(voteRepository.findByWatchParty(wp)).thenReturn(List.of(v));

        assertTrue(voteService.hasAlreadyVoted(wp, p1));
        assertFalse(voteService.hasAlreadyVoted(wp, p2));
    }

    @Test
    void retractVote_deletesUserVotesAndReturnsTrue() {
        Vote v = new Vote(); v.setWatchParty(wp); v.setProfile(p1); v.setMovieId(10);
        when(voteRepository.findByWatchParty(wp)).thenReturn(List.of(v));

        boolean ok = voteService.retractVote(wp, p1);

        assertTrue(ok);
        ArgumentCaptor<List<Vote>> captor = ArgumentCaptor.forClass((Class) List.class);
        verify(voteRepository).deleteAll(captor.capture());
        List<Vote> captured = captor.getValue();
        assertEquals(1, captured.size());
        assertSame(v, captured.get(0));
    }

    @Test
    void retractVote_returnsFalseWhenUserHasNoVotes() {
        Vote v = new Vote(); v.setWatchParty(wp); v.setProfile(p2); v.setMovieId(10);
        when(voteRepository.findByWatchParty(wp)).thenReturn(List.of(v));

        boolean ok = voteService.retractVote(wp, p1);

        assertFalse(ok);
        verify(voteRepository, never()).deleteAll(anyList());
    }

    @Test
    void finalizeVotes_returnsWinner() {
        Vote v1 = new Vote(); v1.setWatchParty(wp); v1.setMovieId(10); v1.setProfile(p1);
        Vote v2 = new Vote(); v2.setWatchParty(wp); v2.setMovieId(10); v2.setProfile(p2);
        Vote v3 = new Vote(); v3.setWatchParty(wp); v3.setMovieId(20); v3.setProfile(new Profile());
        when(voteRepository.findByWatchParty(wp)).thenReturn(List.of(v1, v2, v3));

        Integer winner = voteService.finalizeVotes(wp);
        assertEquals(10, winner);
    }

    @Test
    void deleteAllVotes_whenVotesExist_deletesAndReturnsTrue() {
        Vote v = new Vote(); v.setWatchParty(wp); v.setProfile(p1); v.setMovieId(10);
        when(voteRepository.findByWatchParty(wp)).thenReturn(List.of(v));

        boolean ok = voteService.deleteAllVotes(wp);

        assertTrue(ok);
        ArgumentCaptor<List<Vote>> captor = ArgumentCaptor.forClass((Class) List.class);
        verify(voteRepository).deleteAll(captor.capture());
        List<Vote> captured = captor.getValue();
        assertEquals(1, captured.size());
        assertSame(v, captured.get(0));
    }

    @Test
    void deleteAllVotes_whenNoVotes_returnsFalse() {
        when(voteRepository.findByWatchParty(wp)).thenReturn(List.of());

        boolean ok = voteService.deleteAllVotes(wp);

        assertFalse(ok);
        verify(voteRepository, never()).deleteAll(anyList());
    }
}
