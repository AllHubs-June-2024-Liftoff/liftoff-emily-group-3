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
    private boolean isPrivate;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany
    private List<User> friends;

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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "friends=" + friends +
                ", image='" + image + '\'' +
                ", bio='" + bio + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
