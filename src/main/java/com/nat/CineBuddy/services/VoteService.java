package com.nat.CineBuddy.services;

import com.nat.CineBuddy.models.Profile;
import com.nat.CineBuddy.models.Vote;
import com.nat.CineBuddy.models.WatchParty;
import com.nat.CineBuddy.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    /**
     * Cast a vote for a movie.
     *
     * @param watchParty The group where the vote is being cast.
     * @param movieId    The ID of the movie being voted for.
     * @return True if the vote is successfully cast, false if the user already voted.
     */
    public boolean castVote(WatchParty watchParty, Integer movieId, Profile profile) {
        // NEW: Fast duplicate check (no in-memory scan)
        if (voteRepository.existsByWatchPartyAndProfile(watchParty, profile)) {
            return false;
        }

        Vote vote = new Vote();
        vote.setWatchParty(watchParty);
        vote.setProfile(profile);
        vote.setMovieId(movieId);
        voteRepository.save(vote);

        return true;
    }

    /**
     * Get the most voted movie in a group.
     *
     * @param watchParty The group whose votes are being checked.
     * @return The ID of the most voted movie.
     */
    public Integer getMostVotedMovie(WatchParty watchParty) {
        return getMostVotedMovieId(watchParty)
                .orElseThrow(() -> new IllegalStateException("No votes yet"));
    }

    /**
     * Deterministic winner: ties resolved by picking the lower movieId.
     */
    public Optional<Integer> getMostVotedMovieId(WatchParty watchParty) {
        Map<Integer, Integer> voteCounts = getAllVoteCounts(watchParty);
        if (voteCounts.isEmpty()) return Optional.empty();

        // Max by (count desc), then by (movieId asc) to break ties deterministically
        return voteCounts.entrySet().stream()
                .max((a, b) -> {
                    int cmp = Integer.compare(a.getValue(), b.getValue());
                    if (cmp != 0) return cmp;
                    // when counts equal, prefer lower movieId
                    return Integer.compare(b.getKey(), a.getKey()) * -1;
                })
                .map(Map.Entry::getKey);
    }

    /**
     * Get the current vote counts for all movies in a group.
     *
     * @param watchParty The group whose votes are being checked.
     * @return An immutable map of movie IDs to their vote counts.
     */
    public Map<Integer, Integer> getAllVoteCounts(WatchParty watchParty) {
        List<Vote> votes = voteRepository.findByWatchParty(watchParty);
        Map<Integer, Integer> voteCounts = new LinkedHashMap<>();

        for (Vote vote : votes) {
            voteCounts.merge(vote.getMovieId(), 1, Integer::sum);
        }
        return Map.copyOf(voteCounts); // immutable view prevents accidental mutation
    }

    /**
     * @param watchParty Passing in watchParty to get all votes.
     * @return All vote objects associated with the watchparty
     */
    public List<Vote> getAllVotes(WatchParty watchParty) {
        return voteRepository.findByWatchParty(watchParty);
    }

    /**
     * @param watchParty object passed in.
     * @return true if any vote matches the profile.
     */
    public boolean hasAlreadyVoted(WatchParty watchParty, Profile profile) {
        return getAllVotes(watchParty).stream().anyMatch(vote -> vote.getProfile().equals(profile));
    }

    /**
     * @param watchParty object passed in. Gets profile
     * @return false if no votes, if votes delete all.
     */
    public boolean retractVote(WatchParty watchParty, Profile profile) {
        List<Vote> userVotes = getAllVotes(watchParty).stream()
                .filter(vote -> vote.getProfile().equals(profile)).toList();

        if (userVotes.isEmpty()) {
            return false;
        }

        voteRepository.deleteAll(userVotes);
        return true;
    }

    /**
     * @param watchParty pass in watchParty object.
     * @return most voted movie.
     */
    public Integer finalizeVotes(WatchParty watchParty) {
        return getMostVotedMovie(watchParty);
    }

    /**
     * @param watchParty take watchparty as object for specific watchparty.
     * @return true if votes exist and all votes are deleted in voteRepository.
     */
    public boolean deleteAllVotes(WatchParty watchParty) {
        List<Vote> allVotes = voteRepository.findByWatchParty(watchParty);

        if (allVotes.isEmpty()) {
            return false; // No votes to delete
        }

        voteRepository.deleteAll(allVotes);
        return true;
    }
}
