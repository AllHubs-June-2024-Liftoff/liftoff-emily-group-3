package com.nat.CineBuddy.models;

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
    private User user;
    @ManyToMany
    private List<WatchParty> joinedGroups;
    @OneToMany(mappedBy = "leader")
    private List<WatchParty> hostedGroups;

    public Integer getId() {
        return id;
    }

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

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", bio='" + bio + '\'' +
                ", image='" + image + '\'' +
                ", hidden=" + hidden +
                ", joinedGroups=" + joinedGroups +
                ", hostedGroups=" + hostedGroups +
                '}';
    }
}
