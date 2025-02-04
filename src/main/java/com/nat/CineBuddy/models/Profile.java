package com.nat.CineBuddy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;


@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String bio;
    private String image;
    private boolean hidden;
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @ManyToMany(mappedBy = "members", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    private List<WatchParty> joinedGroups;
    @OneToMany(mappedBy = "leader", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    private List<WatchParty> hostedGroups;

    @OneToMany(mappedBy = "profile", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    private List<Vote> votes;

    @OneToMany(mappedBy = "profile", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    private List<WatchList> watchLists;

    @OneToMany(mappedBy = "profile", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "profile", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    private List<Badge> badges;

    public List<WatchList> getWatchLists() {
        return watchLists;
    }

    public void setWatchLists(List<WatchList> watchLists) {
        this.watchLists = watchLists;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    } //Added for VoteService Test.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<WatchParty> getJoinedGroups() {
        return joinedGroups;
    }

    public void setJoinedGroups(List<WatchParty> joinedGroups) {
        this.joinedGroups = joinedGroups;
    }

    public List<WatchParty> getHostedGroups() {
        return hostedGroups;
    }

    public void setHostedGroups(List<WatchParty> hostedGroups) {
        this.hostedGroups = hostedGroups;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bio='" + bio + '\'' +
                ", image='" + image + '\'' +
                ", hidden=" + hidden +
                ", joinedGroups=" + joinedGroups +
                ", hostedGroups=" + hostedGroups +
                ", votes=" + votes.size() +
                '}';
    }

    //Added for VoteService Test treats two profile objects with same id the same.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return id != null && id.equals(profile.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /*Searches profile votes to determine if the user has voted in a watchparty.*/
    public String movieVotedForInWatchParty(WatchParty watchParty){
        for(Vote vote: this.votes){
            if(vote.getWatchParty().equals(watchParty)){
               return String.valueOf(vote.getMovieId());
            }
        }
        return "-1";
    }
}
