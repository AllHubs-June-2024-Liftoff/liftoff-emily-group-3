package com.nat.CineBuddy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class UserRegistrationDTO {
    @NotEmpty(message="Name is required")
    private String name;
    @NotEmpty(message="Username is required")
    private String username;
    @NotEmpty(message="Email is required")
    @Email(message="Invalid email address")
    private String email;
    @NotEmpty(message="Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    private Set<Integer> roles;

    public UserRegistrationDTO() {
    }

    public UserRegistrationDTO(String name, String username, String email, String password, Set<Integer> roles) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Integer> getRoles() {
        return roles;
    }

    public void setRoles(Set<Integer> roles) {
        this.roles = roles;
    }
}
