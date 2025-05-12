package com.sd.demo.Entities;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String username;
    private Set<String> following = new HashSet<>();

    public User() {}

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Set<String> getFollowing() {
        return following;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void follow(String otherUsername) {
        this.following.add(otherUsername);
    }

}
